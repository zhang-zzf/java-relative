package com.github.zzf.dd.repo.redis;


import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.BDDAssertions.then;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-15
 */
/**
 * <pre>
 *     前提: key 中必须有 hashtag
 *      spring-data-redis lettuce + pipeline + mget 与 spring-data-redis lettuce + mget 的使用区别
 *      1. lettuce pipeline + mget 必须保证批量的 key 都在一个 slot 中，否则会报错。优势是 pipeline 异步
 *      1. lettuce mget 不需要保证批量的 key 都在一个 slot 中，可以跨 slot 使用，会把一个 slot 中的 key 分成 [1] 批, 1批使用1个 mget 下发
 *
 *      用法：
 *      1. 批量 key 都在一个 slot 中，使用 分批 + pipeline + mget，单线程异步实现
 *      1. 批量 key 可能不在一个 slot 中 分批 + mget（根据 slot 再次分批）+ 多线程异步
 * </pre>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("mini234b")
@Slf4j
public class RedisBatchTest {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 结论: redis 集群下 spring-data-redis + lettuce
     *       multiGet/multiSet API 可以跨 node 使用
     *
     * <pre>
     *     RT -> Total: 47770ms, avg: 4ms, cnt: 10000
     * </pre>
     *
     * <pre>
     *     lettuce 把 keys 拆成单个 mget / mset 命令 pipeline 发送给 redis node
     *     注意每个 MSET / MGET 命令中只有一个 key
     *     MSET 7 7 MSET 54 54 MSET 65 65 MSET 11 11 MSET 32 32 MSET 94 94 MSET 19 19 MSET 47 47 MSET 10 10 MSET 18 18
     *     MGET 76 MGET 28 MGET 7 MGET 54 MGET 65 MGET 11 MGET 32 MGET 94
     * </pre>
     * <pre>
     *     No.	Time	Source	Src port	Destination	Protocol	Length	Info
     * 145	0.075259	192.168.56.2	54585	192.168.56.20	RESP	96	Request: MSET 25 25
     * 151	0.075977	192.168.56.2	54585	192.168.56.20	RESP	244	Request: MSET 83 83 MSET 72 72 MSET 24 24 MSET 3 3 MSET 50 50 MSET 58 58
     * 157	0.076894	192.168.56.2	54585	192.168.56.20	RESP	276	Request: MSET 61 61 MSET 69 69 MSET 15 15 MSET 36 36 MSET 98 98 MSET 90 90 MSET 43 43
     * 167	0.077401	192.168.56.2	54585	192.168.56.20	RESP	274	Request: MSET 14 14 MSET 21 21 MSET 87 87 MSET 29 29 MSET 76 76 MSET 28 28 MSET 7 7
     * 177	0.078217	192.168.56.2	54585	192.168.56.20	RESP	126	Request: MSET 54 54 MSET 65 65
     * 186	0.079261	192.168.56.2	54585	192.168.56.20	RESP	276	Request: MSET 11 11 MSET 32 32 MSET 94 94 MSET 19 19 MSET 47 47 MSET 10 10 MSET 18 18
     * 194	0.081094	192.168.56.20	9000	192.168.56.2	RESP	71	Response: OK
     * 200	0.088248	192.168.56.20	9000	192.168.56.2	RESP	211	Response: OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK
     * 202	0.089588	192.168.56.2	54585	192.168.56.20	RESP	88	Request: MGET 25
     * 206	0.090043	192.168.56.2	54585	192.168.56.20	RESP	110	Request: MGET 83 MGET 72
     * 209	0.090252	192.168.56.20	9000	192.168.56.2	RESP	78	Response: Array(1)
     * 210	0.090317	192.168.56.2	54585	192.168.56.20	RESP	88	Request: MGET 24
     * 220	0.091608	192.168.56.20	9000	192.168.56.2	RESP	90	Response: Array(1) Array(1)
     * 221	0.091646	192.168.56.2	54585	192.168.56.20	RESP	614	Request: MGET 3 MGET 50 MGET 58 MGET 61 MGET 69 MGET 15 MGET 36 MGET 98 MGET 90 MGET 43 MGET 14 MGET 21 MGET 87 MGET 29 MGET 76 MGET 28 MGET 7 MGET 54 MGET 65 MGET 11 MGET 32 MGET 94 MGET 19 MGET 47 MGET 10
     * 222	0.091870	192.168.56.20	9000	192.168.56.2	RESP	78	Response: Array(1)
     * 223	0.091900	192.168.56.2	54585	192.168.56.20	RESP	88	Request: MGET 18
     * 240	0.095477	192.168.56.20	9000	192.168.56.2	RESP	376	Response: Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1) Array(1)
     * 244	0.124621	192.168.56.2	54585	192.168.56.20	RESP	87	Request: DEL 25
     * 248	0.125572	192.168.56.2	54585	192.168.56.20	RESP	338	Request: DEL 83 DEL 72 DEL 24 DEL 3 DEL 50 DEL 58 DEL 61 DEL 69 DEL 15 DEL 36 DEL 98 DEL 90 DEL 43
     * 254	0.127609	192.168.56.2	54585	192.168.56.20	RESP	401	Request: DEL 14 DEL 21 DEL 87 DEL 29 DEL 76 DEL 28 DEL 7 DEL 54 DEL 65 DEL 11 DEL 32 DEL 94 DEL 19 DEL 47 DEL 10 DEL 18
     * 263	0.132188	192.168.56.20	9000	192.168.56.2	RESP	70	Response: 1
     * 271	0.139524	192.168.56.20	9000	192.168.56.2	RESP	182	Response: 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1
     * </pre>
     *
     * <pre>
     * key 是分布在 redis cluster 不同的 node 上
     * u2004020 ➜  bin for ((i=0;i<100;i++));do
     * echo "GET ${i}" && ./redis-cli -u redis://192.168.56.21:9000/0 get ${i}
     * done
     * GET 0
     * (error) MOVED 13907 192.168.56.21:9002
     * GET 1
     * (error) MOVED 9842 192.168.56.21:9001
     * GET 2
     * (error) MOVED 5649 192.168.56.21:9001
     * GET 3
     * "3"
     * </pre>
     */
    @Test
    public void givenRedisClusterAndLettuce_whenMget_then() {
        Map<String, Integer> map = Stream
            .iterate(0, i -> i + 1)
            .limit(100)
            .collect(toMap(String::valueOf, i -> i));
        // mset
        ValueOperations valueOp = redisTemplate.opsForValue();
        valueOp.multiSet(map);
        // mget
        List list = valueOp.multiGet(map.keySet());
        then(list).containsAll(map.values());
        // clear
        redisTemplate.delete(map.keySet());
    }

    /**
     * 结论: redis 集群下 spring-data-redis + lettuce + hashTag
     *      multiGet / multiSet API 可以跨 node 使用
     *
     * <pre>
     *     hashtag key 批量下发
     *     lettuce 把 keys 分批（同一个 slot ) 分为 [1] 批, 1批使用1个 mset / mget 下发
     *     {0} / {4} 是路由到一个 node 的不同 slot 中
     *     注意每个 MGET/MSET 命令中包含同一个 slot 下的多个 key
     *     152	0.078730	192.168.56.2	54175	192.168.56.20	RESP	1062	Request: MSET {0}:26 26 {0}:49 49 {0}:14 14 {0}:1 1 {0}:47 47 {0}:27 27 {0}:10 10 {0}:8 8 {0}:31 31 {0}:11 11 {0}:9 9 {0}:21 21 {0}:46 46 {0}:37 37 {0}:15 15 {0}:13 13 {0}:0 0 {0}:44 44 {0}:12 12 {0}:6 6 {0}:20 20 {0}:19 19 {0}:4 4 {0}:48 48 {0}:36 36 {0}:39 39 {0}:18 18 {0}:2 2 {0}:33 33 {0}:45 45 {0}:16 16 {0}:43 43 {0}:3 3 {0}:32 32 {0}:41 41 {0}:35 35 {0}:5 5 {0}:7 7 {0}:17 17 {0}:23 23 {0}:24 24 {0}:40 40 {0}:42 42 {0}:30 30 {0}:28 28 {0}:29 29 {0}:22 22 {0}:34 34 {0}:25 25 {0}:38 38
     *     154	0.079634	192.168.56.2	54175	192.168.56.20	RESP	1082	Request: MSET {4}:66 66 {4}:96 96 {4}:69 69 {4}:67 67 {4}:62 62 {4}:87 87 {4}:83 83 {4}:54 54 {4}:51 51 {4}:82 82 {4}:79 79 {4}:81 81 {4}:80 80 {4}:88 88 {4}:97 97 {4}:85 85 {4}:98 98 {4}:58 58 {4}:99 99 {4}:63 63 {4}:95 95 {4}:94 94 {4}:89 89 {4}:65 65 {4}:70 70 {4}:72 72 {4}:93 93 {4}:86 86 {4}:91 91 {4}:59 59 {4}:60 60 {4}:61 61 {4}:90 90 {4}:77 77 {4}:84 84 {4}:56 56 {4}:53 53 {4}:50 50 {4}:78 78 {4}:55 55 {4}:52 52 {4}:71 71 {4}:75 75 {4}:76 76 {4}:73 73 {4}:57 57 {4}:92 92 {4}:68 68 {4}:64 64 {4}:74 74
     *     156	0.080648	192.168.56.20	9002	192.168.56.2	RESP	71	Response: OK
     *     158	0.081188	192.168.56.20	9002	192.168.56.2	RESP	71	Response: OK
     *     MGET
     *     160	0.082210	192.168.56.2	54175	192.168.56.20	RESP	671	Request: MGET {0}:11 {0}:12 {0}:10 {0}:15 {0}:16 {0}:13 {0}:14 {0}:19 {0}:17 {0}:18 {0}:22 {0}:23 {0}:20 {0}:21 {0}:26 {0}:27 {0}:24 {0}:25 {0}:28 {0}:29 {0}:6 {0}:7 {0}:8 {0}:9 {0}:2 {0}:3 {0}:4 {0}:5 {0}:30 {0}:33 {0}:34 {0}:31 {0}:32 {0}:37 {0}:38 {0}:35 {0}:36 {0}:39 {0}:0 {0}:1 {0}:40 {0}:41 {0}:44 {0}:45 {0}:42 {0}:43 {0}:48 {0}:49 {0}:46 {0}:47
     *     162	0.082721	192.168.56.2	54175	192.168.56.20	RESP	681	Request: MGET {4}:95 {4}:96 {4}:93 {4}:94 {4}:99 {4}:97 {4}:98 {4}:91 {4}:92 {4}:90 {4}:84 {4}:85 {4}:82 {4}:83 {4}:88 {4}:89 {4}:86 {4}:87 {4}:80 {4}:81 {4}:79 {4}:73 {4}:74 {4}:71 {4}:72 {4}:77 {4}:78 {4}:75 {4}:76 {4}:70 {4}:68 {4}:69 {4}:62 {4}:63 {4}:60 {4}:61 {4}:66 {4}:67 {4}:64 {4}:65 {4}:59 {4}:57 {4}:58 {4}:51 {4}:52 {4}:50 {4}:55 {4}:56 {4}:53 {4}:54
     *     163	0.082891	192.168.56.20	9002	192.168.56.2	RESP	461	Response: Array(50)
     *     166	0.083481	192.168.56.20	9002	192.168.56.2	RESP	471	Response: Array(50)
     *     // DEL
     *     168	0.108585	192.168.56.2	54175	192.168.56.20	RESP	670	Request: DEL {0}:11 {0}:12 {0}:10 {0}:15 {0}:16 {0}:13 {0}:14 {0}:19 {0}:17 {0}:18 {0}:22 {0}:23 {0}:20 {0}:21 {0}:26 {0}:27 {0}:24 {0}:25 {0}:28 {0}:29 {0}:6 {0}:7 {0}:8 {0}:9 {0}:2 {0}:3 {0}:4 {0}:5 {0}:30 {0}:33 {0}:34 {0}:31 {0}:32 {0}:37 {0}:38 {0}:35 {0}:36 {0}:39 {0}:0 {0}:1 {0}:40 {0}:41 {0}:44 {0}:45 {0}:42 {0}:43 {0}:48 {0}:49 {0}:46 {0}:47
     *     170	0.109543	192.168.56.2	54175	192.168.56.20	RESP	680	Request: DEL {4}:95 {4}:96 {4}:93 {4}:94 {4}:99 {4}:97 {4}:98 {4}:91 {4}:92 {4}:90 {4}:84 {4}:85 {4}:82 {4}:83 {4}:88 {4}:89 {4}:86 {4}:87 {4}:80 {4}:81 {4}:79 {4}:73 {4}:74 {4}:71 {4}:72 {4}:77 {4}:78 {4}:75 {4}:76 {4}:70 {4}:68 {4}:69 {4}:62 {4}:63 {4}:60 {4}:61 {4}:66 {4}:67 {4}:64 {4}:65 {4}:59 {4}:57 {4}:58 {4}:51 {4}:52 {4}:50 {4}:55 {4}:56 {4}:53 {4}:54
     *     172	0.110694	192.168.56.20	9002	192.168.56.2	RESP	71	Response: 50
     *     174	0.111535	192.168.56.20	9002	192.168.56.2	RESP	71	Response: 50
     * </pre>
     */
    @Test
    public void givenRedisClusterAndLettuce_whenMgetWithHashtag_then() {
        Map<String, Integer> map = Stream
            .iterate(0, i -> i + 1)
            .limit(100)
            .collect(toMap(i -> i < 50 ? "{0}:" + i : "{4}:" + i, i -> i));
        // mset
        ValueOperations valueOp = redisTemplate.opsForValue();
        valueOp.multiSet(map);
        // mget
        List list = valueOp.multiGet(map.keySet());
        then(list).containsAll(map.values());
        // clear
        redisTemplate.delete(map.keySet());
    }

    /**
     * <pre>
     *     spring-data-redis + lettuce
     *     multiGet / multiSet API 可与跨 slot 也可以跨 node 使用
     *
     *     1. 有 hashtag 的场景
     *
     *     {0} / {4} 路由到 9002
     *     No.	Time	Source	Src port	Destination	Protocol	Length	Info
     * 175	0.077631	192.168.56.2	54340	192.168.56.20	RESP	477	Request: MSET {0}:20 20 {0}:50 50 {0}:10 10 {0}:15 15 {0}:80 80 {0}:95 95 {0}:85 85 {0}:0 0 {0}:90 90 {0}:5 5 {0}:30 30 {0}:60 60 {0}:35 35 {0}:75 75 {0}:25 25 {0}:55 55 {0}:40 40 {0}:65 65 {0}:45 45 {0}:70 70
     * 182	0.079082	192.168.56.2	54340	192.168.56.20	RESP	477	Request: MSET {4}:84 84 {4}:99 99 {4}:69 69 {4}:49 49 {4}:29 29 {4}:59 59 {4}:9 9 {4}:74 74 {4}:44 44 {4}:94 94 {4}:4 4 {4}:79 79 {4}:54 54 {4}:24 24 {4}:19 19 {4}:64 64 {4}:34 34 {4}:14 14 {4}:89 89 {4}:39 39
     * 185	0.079882	192.168.56.20	9002	192.168.56.2	RESP	71	Response: OK
     * 187	0.080261	192.168.56.20	9002	192.168.56.2	RESP	71	Response: OK
     * 193	0.082125	192.168.56.2	54340	192.168.56.20	RESP	319	Request: MGET {0}:90 {0}:95 {0}:10 {0}:15 {0}:20 {0}:25 {0}:70 {0}:75 {0}:80 {0}:85 {0}:50 {0}:55 {0}:5 {0}:60 {0}:65 {0}:30 {0}:35 {0}:0 {0}:40 {0}:45
     * 200	0.083365	192.168.56.2	54340	192.168.56.20	RESP	319	Request: MGET {4}:24 {4}:29 {4}:9 {4}:14 {4}:19 {4}:94 {4}:99 {4}:84 {4}:89 {4}:4 {4}:79 {4}:74 {4}:69 {4}:64 {4}:59 {4}:54 {4}:49 {4}:44 {4}:39 {4}:34
     * 202	0.083595	192.168.56.20	9002	192.168.56.2	RESP	229	Response: Array(20)
     * 209	0.084322	192.168.56.20	9002	192.168.56.2	RESP	229	Response: Array(20)
     * 211	0.113859	192.168.56.2	54340	192.168.56.20	RESP	318	Request: DEL {0}:90 {0}:95 {0}:10 {0}:15 {0}:20 {0}:25 {0}:70 {0}:75 {0}:80 {0}:85 {0}:50 {0}:55 {0}:5 {0}:60 {0}:65 {0}:30 {0}:35 {0}:0 {0}:40 {0}:45
     * 215	0.115127	192.168.56.2	54340	192.168.56.20	RESP	318	Request: DEL {4}:24 {4}:29 {4}:9 {4}:14 {4}:19 {4}:94 {4}:99 {4}:84 {4}:89 {4}:4 {4}:79 {4}:74 {4}:69 {4}:64 {4}:59 {4}:54 {4}:49 {4}:44 {4}:39 {4}:34
     * 217	0.115813	192.168.56.20	9002	192.168.56.2	RESP	71	Response: 20
     * 225	0.117266	192.168.56.20	9002	192.168.56.2	RESP	71	Response: 20
     *
     *  {3} 路由到 9000 
     *  No.	Time	Source	Src port	Destination	Protocol	Length	Info
     * 170	0.076900	192.168.56.2	54338	192.168.56.20	RESP	477	Request: MSET {3}:38 38 {3}:73 73 {3}:93 93 {3}:18 18 {3}:63 63 {3}:43 43 {3}:33 33 {3}:23 23 {3}:98 98 {3}:83 83 {3}:53 53 {3}:78 78 {3}:3 3 {3}:88 88 {3}:28 28 {3}:13 13 {3}:8 8 {3}:58 58 {3}:68 68 {3}:48 48
     * 179	0.078869	192.168.56.20	9000	192.168.56.2	RESP	71	Response: OK
     * 191	0.081881	192.168.56.2	54338	192.168.56.20	RESP	319	Request: MGET {3}:68 {3}:63 {3}:58 {3}:53 {3}:48 {3}:43 {3}:38 {3}:33 {3}:3 {3}:28 {3}:23 {3}:93 {3}:18 {3}:98 {3}:13 {3}:8 {3}:88 {3}:83 {3}:78 {3}:73
     * 197	0.082948	192.168.56.20	9000	192.168.56.2	RESP	229	Response: Array(20)
     * 212	0.113980	192.168.56.2	54338	192.168.56.20	RESP	318	Request: DEL {3}:68 {3}:63 {3}:58 {3}:53 {3}:48 {3}:43 {3}:38 {3}:33 {3}:3 {3}:28 {3}:23 {3}:93 {3}:18 {3}:98 {3}:13 {3}:8 {3}:88 {3}:83 {3}:78 {3}:73
     * 223	0.117114	192.168.56.20	9000	192.168.56.2	RESP	71	Response: 20
     *
     * {1} / {2} 路由到 9001
     * No.	Time	Source	Src port	Destination	Protocol	Length	Info
     * 174	0.077507	192.168.56.2	54339	192.168.56.20	RESP	477	Request: MSET {2}:97 97 {2}:2 2 {2}:52 52 {2}:42 42 {2}:7 7 {2}:17 17 {2}:47 47 {2}:57 57 {2}:62 62 {2}:72 72 {2}:37 37 {2}:22 22 {2}:92 92 {2}:12 12 {2}:77 77 {2}:87 87 {2}:82 82 {2}:67 67 {2}:32 32 {2}:27 27
     * 178	0.078821	192.168.56.2	54339	192.168.56.20	RESP	477	Request: MSET {1}:11 11 {1}:36 36 {1}:76 76 {1}:26 26 {1}:16 16 {1}:86 86 {1}:81 81 {1}:56 56 {1}:21 21 {1}:66 66 {1}:1 1 {1}:41 41 {1}:31 31 {1}:51 51 {1}:71 71 {1}:46 46 {1}:6 6 {1}:96 96 {1}:91 91 {1}:61 61
     * 189	0.080766	192.168.56.20	9001	192.168.56.2	RESP	76	Response: OK OK
     * 192	0.081881	192.168.56.2	54339	192.168.56.20	RESP	319	Request: MGET {2}:17 {2}:12 {2}:97 {2}:92 {2}:2 {2}:27 {2}:22 {2}:77 {2}:72 {2}:87 {2}:82 {2}:57 {2}:52 {2}:67 {2}:62 {2}:37 {2}:32 {2}:7 {2}:47 {2}:42
     * 196	0.082893	192.168.56.2	54339	192.168.56.20	RESP	319	Request: MGET {1}:56 {1}:51 {1}:66 {1}:61 {1}:31 {1}:1 {1}:36 {1}:41 {1}:46 {1}:6 {1}:96 {1}:11 {1}:16 {1}:91 {1}:21 {1}:26 {1}:76 {1}:71 {1}:86 {1}:81
     * 204	0.083697	192.168.56.20	9001	192.168.56.2	RESP	229	Response: Array(20)
     * 207	0.084235	192.168.56.20	9001	192.168.56.2	RESP	229	Response: Array(20)
     * 213	0.113981	192.168.56.2	54339	192.168.56.20	RESP	318	Request: DEL {2}:17 {2}:12 {2}:97 {2}:92 {2}:2 {2}:27 {2}:22 {2}:77 {2}:72 {2}:87 {2}:82 {2}:57 {2}:52 {2}:67 {2}:62 {2}:37 {2}:32 {2}:7 {2}:47 {2}:42
     * 220	0.115951	192.168.56.2	54339	192.168.56.20	RESP	318	Request: DEL {1}:56 {1}:51 {1}:66 {1}:61 {1}:31 {1}:1 {1}:36 {1}:41 {1}:46 {1}:6 {1}:96 {1}:11 {1}:16 {1}:91 {1}:21 {1}:26 {1}:76 {1}:71 {1}:86 {1}:81
     * 227	0.117418	192.168.56.20	9001	192.168.56.2	RESP	71	Response: 20
     * 229	0.118173	192.168.56.20	9001	192.168.56.2	RESP	71	Response: 20
     * </pre>
     */
    @Test
    public void givenRedisClusterAndLettuce_whenMgetWithHashtag2_then() {
        Map<String, Integer> map = Stream
            .iterate(0, i -> i + 1)
            .limit(100)
            .collect(toMap(i -> "{" + i % 5 + "}:" + i, i -> i));
        // mset
        ValueOperations valueOp = redisTemplate.opsForValue();
        valueOp.multiSet(map);
        // mget
        List list = valueOp.multiGet(map.keySet());
        then(list).containsAll(map.values());
        // clear
        redisTemplate.delete(map.keySet());
    }

    /**
     * 结论: redis 集群下 spring-data-redis + lettuce
     *      pipeline + multiGet / multiSet API 可以跨 node 使用
     *
     * <pre>
     *     lettuce mset 把 keys 分批（同一个 slot ) 分为 [1] 批, 1批使用1个 mset / mget 下发
     *     {0} / {4} 是路由到一个 node 的不同 slot 中
     *     注意每个 MGET/MSET 命令中包含同一个 slot 下的多个 key
     *     multiSet 自动分组,把同一个 slot 中的 key 分成 [1] 批, 1批使用1个 mset 下发
     *     143	0.074794	192.168.56.2	53362	192.168.56.20	RESP	1062	Request: MSET {0}:22 22 {0}:19 19 {0}:45 45 {0}:10 10 {0}:35 35 {0}:29 29 {0}:49 49 {0}:5 5 {0}:28 28 {0}:43 43 {0}:20 20 {0}:42 42 {0}:33 33 {0}:12 12 {0}:38 38 {0}:0 0 {0}:46 46 {0}:15 15 {0}:8 8 {0}:44 44 {0}:32 32 {0}:37 37 {0}:13 13 {0}:4 4 {0}:6 6 {0}:14 14 {0}:18 18 {0}:17 17 {0}:1 1 {0}:23 23 {0}:2 2 {0}:41 41 {0}:7 7 {0}:30 30 {0}:39 39 {0}:27 27 {0}:31 31 {0}:9 9 {0}:25 25 {0}:16 16 {0}:48 48 {0}:11 11 {0}:26 26 {0}:36 36 {0}:40 40 {0}:24 24 {0}:3 3 {0}:47 47 {0}:21 21 {0}:34 34
     *     145	0.075518	192.168.56.2	53362	192.168.56.20	RESP	1082	Request: MSET {4}:62 62 {4}:75 75 {4}:63 63 {4}:76 76 {4}:83 83 {4}:50 50 {4}:86 86 {4}:89 89 {4}:64 64 {4}:88 88 {4}:54 54 {4}:65 65 {4}:98 98 {4}:66 66 {4}:84 84 {4}:97 97 {4}:96 96 {4}:90 90 {4}:93 93 {4}:95 95 {4}:70 70 {4}:56 56 {4}:85 85 {4}:61 61 {4}:77 77 {4}:79 79 {4}:82 82 {4}:68 68 {4}:69 69 {4}:73 73 {4}:67 67 {4}:51 51 {4}:52 52 {4}:99 99 {4}:57 57 {4}:74 74 {4}:58 58 {4}:59 59 {4}:81 81 {4}:55 55 {4}:92 92 {4}:94 94 {4}:71 71 {4}:72 72 {4}:53 53 {4}:87 87 {4}:91 91 {4}:78 78 {4}:60 60 {4}:80 80
     *     pipeline + mget 注意一个pipeline 中的 mget key1 key2 ... keyn 必须在一个 slot 中
     *     10个      225	0.098945	192.168.56.2	53364	192.168.56.20	RESP	201	Request: MGET {0}:11 {0}:12 {0}:10 {0}:15 {0}:16 {0}:13 {0}:14 {0}:19 {0}:17 {0}:18
     *     10个*4    227	0.099623	192.168.56.2	53364	192.168.56.20	RESP	596	Request: MGET {0}:22 {0}:23 {0}:20 {0}:21 {0}:26 {0}:27 {0}:24 {0}:25 {0}:28 {0}:29 MGET {0}:6 {0}:7 {0}:8 {0}:9 {0}:2 {0}:3 {0}:4 {0}:5 {0}:30 {0}:33 MGET {0}:34 {0}:31 {0}:32 {0}:37 {0}:38 {0}:35 {0}:36 {0}:39 {0}:0 {0}:1 MGET {0}:40 {0}:41 {0}:44 {0}:45 {0}:42 {0}:43 {0}:48 {0}:49 {0}:46 {0}:47
     *     10个*5    229	0.100790	192.168.56.20	9002	192.168.56.2	RESP	481	Response: Array(10) Array(10) Array(10) Array(10) Array(10)
     * </pre>
     */
    @Test
    public void givenRedisClusterAndLettuce_whenPipelineAndMgetWithHashtag_then() {
        Map<String, Integer> map = Stream
            .iterate(0, i -> i + 1)
            .limit(100)
            .collect(toMap(i -> i < 50 ? "{0}:" + i : "{4}:" + i, i -> i));
        // mset
        ValueOperations valueOp = redisTemplate.opsForValue();
        valueOp.multiSet(map);
        // mget
        // List list = valueOp.multiGet(map.keySet());
        // lettuce pipeline 底层使用异步 实测 lettuce key 可以跨 node
        List list = pipelineMultiGet(map.keySet().stream().filter(key -> key.startsWith("{0}")).collect(toList()));
        list.addAll(pipelineMultiGet(map.keySet().stream().filter(key -> key.startsWith("{4}")).collect(toList())));
        // key 不在一个 slot 中会报错
        // List list = pipelineMultiGet(new ArrayList<>(map.keySet()));
        // then
        then(list).containsAll(map.values());
        // clear
        redisTemplate.delete(map.keySet());
    }

    /**
     * 核心点： keys 必须在同一个 slot 中
     */
    private List<Object> pipelineMultiGet(List<String> keys) {
        List<Object> cachedData = redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                // noinspection ResultOfMethodCallIgnored
                Lists.partition(keys, 10).stream()
                    .map(keys -> operations.opsForValue().multiGet((Collection<K>) keys))
                    .collect(toList());
                return null;
            }
        });
        return ofNullable(cachedData).stream()
            .flatMap(Collection::stream)
            .filter(Objects::nonNull)
            .map(obj -> (List<Object>) obj)
            .flatMap(Collection::stream)
            .filter(Objects::nonNull)
            .collect(toList());
    }

    /**
     * 结论: redis 集群下 spring-data-redis + lettuce pipeline+get/set 命令可以跨 slot/node 使用 pipeline 只是在1个 tcp packet
     * 发送中多条命令，在客户端和 redis-server 端没有做任何特殊处理。 在 redis-server 看来，pipeline 1个 tcp packet 发过来的多个命令，和使用多个 tcp packet
     * 发送的多个命令没有任务区别
     *
     * <pre>
     *     lettuce 把 key 分批异步发送到 redis-server 的一个 node（同一批命令必须是归属在同一个 node下的）
     *     每次发送可能只发送一个 GET / SET 命令，也有可能发送多个 GET/SET 命令
     *     注意发送到同一个 node 的一批命令可以归属到不同的 slot 中
     *     // pipeline SET
     *     216	0.103890	192.168.56.2	192.168.56.21	RESP	298	Request: SET 94 94 SET 10 10 SET 98 98 SET 11 11 SET 14 14 SET 15 15 SET 18 18 SET 19 19
     *     232	0.104624	192.168.56.2	192.168.56.21	RESP	180	Request: SET 7 7 SET 21 21 SET 24 24 SET 25 25
     *     // pipeline GET
     *     322	0.113376	192.168.56.2	192.168.56.21	RESP	231	Request: GET 89 GET 93 GET 97 GET 13 GET 17 GET 0 GET 4 GET 8
     *     326	0.113616	192.168.56.2	192.168.56.21	RESP	129	Request: GET 22 GET 26 GET 31
     *
     *     //DEL
     *     424	0.128832	192.168.56.2	192.168.56.21	RESP	234	Request: DEL 65 DEL 11 DEL 32 DEL 94 DEL 19 DEL 47 DEL 10 DEL 18
     *     425	0.128832	192.168.56.2	192.168.56.21	RESP	128	Request: DEL 99 DEL 9 DEL 91
     * </pre>
     */
    @Test
    public void givenRedisClusterAndLettuce_whenGetWithPipeline_then() {
        Map<String, Integer> map = Stream
            .iterate(0, i -> i + 1)
            .limit(100)
            .collect(toMap(String::valueOf, i -> i));
        // mset
        redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // class io.lettuce.core.cluster.PipelinedRedisFuture cannot be cast to class io.lettuce.core.protocol.RedisCommand (io.lettuce.core.cluster.PipelinedRedisFuture and io.lettuce.core.protocol.RedisCommand are in unnamed module of loader 'app')
                // operations.opsForValue().multiSet(map);
                map.forEach(operations.opsForValue()::set);
                return null;
            }
        });
        List list = redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // operations.opsForValue().multiGet(map.keySet());
                map.keySet().forEach(operations.opsForValue()::get);
                return null;
            }
        });
        // mget
        then(list).containsAll(map.values());
        // clear
        redisTemplate.delete(map.keySet());
    }

    /**
     * <pre>
     *     结论: redis 集群下 spring-data-redis + lettuce scan 可以扫描整个集群中的所有 key
     *
     * </pre>
     */
    @Test
    public void givenRedisClusterAndLettuce_whenScan_then() {
        Map<String, Integer> map = Stream
            .iterate(0, i -> i + 1)
            .limit(100)
            .collect(toMap(String::valueOf, i -> i));
        // mset
        redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                map.forEach(operations.opsForValue()::set);
                return null;
            }
        });
        final String pattern01 = "1*";
        Integer keyDeleted = (Integer) redisTemplate.execute((RedisCallback) connection -> {
            int count = 0;
            ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern01).build();
            try (Cursor<byte[]> cursor = connection.scan(scanOptions)) {
                while (cursor.hasNext()) { // 删除所有满足条件的 key
                    byte[] key = cursor.next(); // 返回的是单个 key
                    log.info("scan -> pattern: {}, key: {}", pattern01,
                        redisTemplate.getKeySerializer().deserialize(key));
                    // connection.del(key);
                    count += 1;
                }
            } catch (IOException e) {// ignore
            }
            return count;
        });
        //
        then(keyDeleted).isGreaterThan(0);
        //
        //
        String pattern2 = "2*";
        Cursor<byte[]> cursor = (Cursor<byte[]>) redisTemplate.execute((RedisCallback) connection -> {
            ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern2).build();
            return connection.scan(scanOptions);
        });
        assert cursor != null;
        try (cursor) { // 使用完成后自动关闭 cursor
            while (cursor.hasNext()) {
                byte[] key = cursor.next();
                log.info("scan -> pattern: {}, key: {}", pattern2, redisTemplate.getKeySerializer().deserialize(key));
            }
        } catch (IOException e) {//
        }
        // clear
        redisTemplate.delete(map.keySet());
    }

    @Test
    public void givenRedisClusterAndLettuce_whenMget_thenRT() {
        int CNT = 10000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < CNT; i++) {
            givenRedisClusterAndLettuce_whenMget_then();
        }
        long end = System.currentTimeMillis();
        log.info("RT -> Total: {}, avg: {}, cnt: {}", (end - start), (end - start) / CNT, CNT);
    }

}
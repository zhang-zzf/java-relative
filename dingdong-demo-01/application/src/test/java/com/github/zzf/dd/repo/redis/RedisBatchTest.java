package com.github.zzf.dd.repo.redis;


import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("mini234b")
@Slf4j
public class RedisBatchTest {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 结论: redis 集群下 spring-data-redis + lettuce mget/mget 命令可以跨 node 使用
     *
     * <pre>
     *     RT -> Total: 47770ms, avg: 4ms, cnt: 10000
     * </pre>
     *
     * <pre>
     *     lettuce 把 keys 分批 使用 pipeline 发送给 redis node
     *     注意每个 MSET / MGET 命令中只有一个 key
     *     MSET 7 7 MSET 54 54 MSET 65 65 MSET 11 11 MSET 32 32 MSET 94 94 MSET 19 19 MSET 47 47 MSET 10 10 MSET 18 18
     *     MGET 76 MGET 28 MGET 7 MGET 54 MGET 65 MGET 11 MGET 32 MGET 94
     * </pre>
     * <pre>
     * 0000   08 00 27 b3 52 81 d0 11 e5 8e 24 3b 08 00 45 02   ..'.R.....$;..E.
     * 0010   01 5e 00 00 40 00 40 06 00 00 c0 a8 38 02 c0 a8   .^..@.@.....8...
     * 0020   38 15 e3 16 23 28 44 e0 ad 6d fb 1b 1a 3e 80 18   8...#(D..m...>..
     * 0030   08 0a f2 b8 00 00 01 01 08 0a ba d9 91 9d d5 19   ................
     * 0040   60 d1 2a 33 0d 0a 24 34 0d 0a 4d 53 45 54 0d 0a   `.*3..$4..MSET..
     * 0050   24 31 0d 0a 37 0d 0a 24 31 0d 0a 37 0d 0a 2a 33   $1..7..$1..7..*3
     * 0060   0d 0a 24 34 0d 0a 4d 53 45 54 0d 0a 24 32 0d 0a   ..$4..MSET..$2..
     * 0070   35 34 0d 0a 24 32 0d 0a 35 34 0d 0a 2a 33 0d 0a   54..$2..54..*3..
     * 0080   24 34 0d 0a 4d 53 45 54 0d 0a 24 32 0d 0a 36 35   $4..MSET..$2..65
     * 0090   0d 0a 24 32 0d 0a 36 35 0d 0a 2a 33 0d 0a 24 34   ..$2..65..*3..$4
     * 00a0   0d 0a 4d 53 45 54 0d 0a 24 32 0d 0a 31 31 0d 0a   ..MSET..$2..11..
     * 00b0   24 32 0d 0a 31 31 0d 0a 2a 33 0d 0a 24 34 0d 0a   $2..11..*3..$4..
     * 00c0   4d 53 45 54 0d 0a 24 32 0d 0a 33 32 0d 0a 24 32   MSET..$2..32..$2
     * 00d0   0d 0a 33 32 0d 0a 2a 33 0d 0a 24 34 0d 0a 4d 53   ..32..*3..$4..MS
     * 00e0   45 54 0d 0a 24 32 0d 0a 39 34 0d 0a 24 32 0d 0a   ET..$2..94..$2..
     * 00f0   39 34 0d 0a 2a 33 0d 0a 24 34 0d 0a 4d 53 45 54   94..*3..$4..MSET
     * 0100   0d 0a 24 32 0d 0a 31 39 0d 0a 24 32 0d 0a 31 39   ..$2..19..$2..19
     * 0110   0d 0a 2a 33 0d 0a 24 34 0d 0a 4d 53 45 54 0d 0a   ..*3..$4..MSET..
     * 0120   24 32 0d 0a 34 37 0d 0a 24 32 0d 0a 34 37 0d 0a   $2..47..$2..47..
     * 0130   2a 33 0d 0a 24 34 0d 0a 4d 53 45 54 0d 0a 24 32   *3..$4..MSET..$2
     * 0140   0d 0a 31 30 0d 0a 24 32 0d 0a 31 30 0d 0a 2a 33   ..10..$2..10..*3
     * 0150   0d 0a 24 34 0d 0a 4d 53 45 54 0d 0a 24 32 0d 0a   ..$4..MSET..$2..
     * 0160   31 38 0d 0a 24 32 0d 0a 31 38 0d 0a               18..$2..18..
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
     * 结论: redis 集群下 spring-data-redis + lettuce mget/mget 命令可以跨 node 使用
     *
     * <pre>
     *     lettuce 把 keys 分批（同一个 slot ) 分为 [1] 批, 1批使用1个 mset / mget 下发
     *     {0} / {4} 是路由到一个 node 的不同 slot 中
     *     注意每个 MGET/MSET 命令中包含同一个 slot 下的多个 key
     *     129	0.078779	192.168.56.2	192.168.56.21	RESP	1062	Request: MSET {0}:20 20 {0}:36 36 {0}:1 1 {0}:12 12 {0}:43 43 {0}:27 27 {0}:15 15 {0}:3 3 {0}:2 2 {0}:44 44 {0}:11 11 {0}:30 30 {0}:47 47 {0}:28 28 {0}:37 37 {0}:29 29 {0}:21 21 {0}:45 45 {0}:24 24 {0}:5 5 {0}:7 7 {0}:41 41 {0}:42 42 {0}:16 16 {0}:26 26 {0}:23 23 {0}:0 0 {0}:6 6 {0}:33 33 {0}:34 34 {0}:13 13 {0}:19 19 {0}:32 32 {0}:25 25 {0}:48 48 {0}:46 46 {0}:14 14 {0}:22 22 {0}:9 9 {0}:38 38 {0}:8 8 {0}:10 10 {0}:31 31 {0}:4 4 {0}:39 39 {0}:17 17 {0}:35 35 {0}:18 18 {0}:49 49 {0}:40 40
     *     131	0.079575	192.168.56.2	192.168.56.21	RESP	1082	Request: MSET {4}:70 70 {4}:79 79 {4}:91 91 {4}:75 75 {4}:50 50 {4}:60 60 {4}:80 80 {4}:74 74 {4}:94 94 {4}:55 55 {4}:98 98 {4}:51 51 {4}:59 59 {4}:87 87 {4}:63 63 {4}:68 68 {4}:83 83 {4}:72 72 {4}:84 84 {4}:89 89 {4}:65 65 {4}:95 95 {4}:85 85 {4}:69 69 {4}:76 76 {4}:62 62 {4}:54 54 {4}:90 90 {4}:73 73 {4}:96 96 {4}:64 64 {4}:53 53 {4}:81 81 {4}:71 71 {4}:78 78 {4}:61 61 {4}:82 82 {4}:66 66 {4}:67 67 {4}:97 97 {4}:77 77 {4}:92 92 {4}:93 93 {4}:57 57 {4}:56 56 {4}:52 52 {4}:88 88 {4}:86 86 {4}:99 99 {4}:58 58
     *     MGET
     *     137	0.081489	192.168.56.2	192.168.56.21	RESP	671	Request: MGET {0}:11 {0}:12 {0}:10 {0}:15 {0}:16 {0}:13 {0}:14 {0}:19 {0}:17 {0}:18 {0}:22 {0}:23 {0}:20 {0}:21 {0}:26 {0}:27 {0}:24 {0}:25 {0}:28 {0}:29 {0}:6 {0}:7 {0}:8 {0}:9 {0}:2 {0}:3 {0}:4 {0}:5 {0}:30 {0}:33 {0}:34 {0}:31 {0}:32 {0}:37 {0}:38 {0}:35 {0}:36 {0}:39 {0}:0 {0}:1 {0}:40 {0}:41 {0}:44 {0}:45 {0}:42 {0}:43 {0}:48 {0}:49 {0}:46 {0}:47
     *     139	0.081942	192.168.56.2	192.168.56.21	RESP	681	Request: MGET {4}:95 {4}:96 {4}:93 {4}:94 {4}:99 {4}:97 {4}:98 {4}:91 {4}:92 {4}:90 {4}:84 {4}:85 {4}:82 {4}:83 {4}:88 {4}:89 {4}:86 {4}:87 {4}:80 {4}:81 {4}:79 {4}:73 {4}:74 {4}:71 {4}:72 {4}:77 {4}:78 {4}:75 {4}:76 {4}:70 {4}:68 {4}:69 {4}:62 {4}:63 {4}:60 {4}:61 {4}:66 {4}:67 {4}:64 {4}:65 {4}:59 {4}:57 {4}:58 {4}:51 {4}:52 {4}:50 {4}:55 {4}:56 {4}:53 {4}:54
     *     // DEL
     *     145	0.105596	192.168.56.2	192.168.56.21	RESP	670	Request: DEL {0}:11 {0}:12 {0}:10 {0}:15 {0}:16 {0}:13 {0}:14 {0}:19 {0}:17 {0}:18 {0}:22 {0}:23 {0}:20 {0}:21 {0}:26 {0}:27 {0}:24 {0}:25 {0}:28 {0}:29 {0}:6 {0}:7 {0}:8 {0}:9 {0}:2 {0}:3 {0}:4 {0}:5 {0}:30 {0}:33 {0}:34 {0}:31 {0}:32 {0}:37 {0}:38 {0}:35 {0}:36 {0}:39 {0}:0 {0}:1 {0}:40 {0}:41 {0}:44 {0}:45 {0}:42 {0}:43 {0}:48 {0}:49 {0}:46 {0}:47
     *     147	0.106241	192.168.56.2	192.168.56.21	RESP	680	Request: DEL {4}:95 {4}:96 {4}:93 {4}:94 {4}:99 {4}:97 {4}:98 {4}:91 {4}:92 {4}:90 {4}:84 {4}:85 {4}:82 {4}:83 {4}:88 {4}:89 {4}:86 {4}:87 {4}:80 {4}:81 {4}:79 {4}:73 {4}:74 {4}:71 {4}:72 {4}:77 {4}:78 {4}:75 {4}:76 {4}:70 {4}:68 {4}:69 {4}:62 {4}:63 {4}:60 {4}:61 {4}:66 {4}:67 {4}:64 {4}:65 {4}:59 {4}:57 {4}:58 {4}:51 {4}:52 {4}:50 {4}:55 {4}:56 {4}:53 {4}:54
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
package com.github.zzf.dd.repo.redis;


import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
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
@ActiveProfiles(profiles = {"mini234b", "jedisCluster"})
@Slf4j
public class RedisBatchWithJedisTest {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 结论: redis 集群下 spring-data-redis + jedis mget/mget 命令可以跨 node 使用
     *
     * <pre>
     *     RT -> Total: 21280, avg: 212, cnt: 100
     * </pre>
     *
     * <pre>
     *    jedis 把 keys 拆分成单个 key，每个 key 使用 SET / GET 请求1次
     *    308	1.318009	192.168.56.2	192.168.56.21	RESP	95	Request: SET 75 75
     *    620	1.370819	192.168.56.2	192.168.56.21	RESP	87	Request: GET 62
     *    722	1.437403	192.168.56.2	192.168.56.21	RESP	87	Request: DEL 65
     *
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
    public void givenRedisClusterAndJedis_whenMget_then() {
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

    @Test
    public void givenRedisClusterAndLettuce_whenMget_thenRT() {
        int CNT = 100;
        long start = System.currentTimeMillis();
        for (int i = 0; i < CNT; i++) {
            givenRedisClusterAndJedis_whenMget_then();
        }
        long end = System.currentTimeMillis();
        log.info("RT -> Total: {}, avg: {}, cnt: {}", (end - start), (end - start) / CNT, CNT);
    }

    /**
     * 结论: redis 集群下 spring-data-redis + jedis mget/mget with hashtag 命令可以跨 node 使用
     *
     * <pre>
     *     jedis 把 keys 拆成单个 key 使用  GET / DEL / DEL 执行，且使用同步一个一个执行命令
     * </pre>
     *
     * <pre>
     *     16	1.377118	192.168.56.2	64735	192.168.56.21	RESP	99	Request: SET {0}:11 11
     *     18	1.378028	192.168.56.21	9002	192.168.56.2	RESP	71	Response: OK
     *     20	1.378346	192.168.56.2	64735	192.168.56.21	RESP	99	Request: SET {0}:12 12
     *     22	1.379135	192.168.56.21	9002	192.168.56.2	RESP	71	Response: OK
     * </pre>
     */
    @Test
    public void givenRedisClusterAndJedis_whenMgetWithHashtag_then() {
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
     * 结论: redis 集群下 spring-data-redis(2.1.21.RELEASE) + jedis 不支持 pipeline
     *
     * <pre>
     *     Pipeline is currently not supported for JedisClusterConnection
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



}
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
import org.springframework.data.redis.core.RedisTemplate;
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
public class RedisConfigTest {

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
     *     lettuce 把 keys 分批（同一个 node）分为 [1..n] 批 使用 pipeline 发送给 redis node
     *     注意每个 MGET 命令中只有一个 key
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
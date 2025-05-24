package com.github.zzf.learn.app.repo.redis.lettuce;

import static org.assertj.core.api.BDDAssertions.then;

import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import org.junit.jupiter.api.Test;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-05-23
 */
public class LettuceTest {


    @Test
    void givenLettuce_whenConnect_thenConnectSuccessfully() {
        RedisClusterClient client = RedisClusterClient.create("redis://192.168.56.20:7000");
        StatefulRedisClusterConnection<String, String> connection = client.connect();
        RedisAdvancedClusterCommands<String, String> sync = connection.sync();
        String val = "value";
        String key = "key";
        then(sync.set(key, val)).isEqualTo("OK");
        then(sync.get(key)).isEqualTo(val);
    }
}

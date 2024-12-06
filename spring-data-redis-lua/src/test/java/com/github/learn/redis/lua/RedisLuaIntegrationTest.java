package com.github.learn.redis.lua;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/22
 */
@SpringBootTest
public class RedisLuaIntegrationTest {

    public static final String LUA = "" +
        "local v_f_n = 'version'\n" +
        "local v_f_v = 'value'\n" +
        "local redis_v = redis.call('hget', KEYS[1], v_f_n)\n" +
        "local version = nil\n" +
        "for k, v in pairs(ARGV) do\n" +
        "    if v_f_n == v then\n" +
        "        version = ARGV[k + 1]\n" +
        "        break\n" +
        "    end\n" +
        "end\n" +
        "if not redis_v or not version or tonumber(cjson.decode(version)[v_f_v]) > tonumber(cjson.decode(redis_v)[v_f_v]) then\n"
        +
        "    for k, v in pairs(ARGV) do\n" +
        "        if k % 2 == 0 then\n" +
        "            redis.call('hset', KEYS[1], ARGV[k - 1], v)\n" +
        "        end" +
        "    end\n" +
        "    redis.call('expire', KEYS[1], 84600)" +
        "    return 1\n" +
        "else\n" +
        "    return 0\n" +
        "end";
    public static final String[] KNIGHT_ONE = {"k:p:1", "version", "{\"value\":3}"};
    public static final String[] KNIGHT_TWO = {"k:p:2", "version", "{\"value\":3}"};
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * redis data connection with jedis as connection library can not use eval with pipeline
     */
    @Test
    void givenLua_whenOperateHash_thenSuccess() {
        Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection()
            .getNativeConnection();
        Pipeline pipelined = jedis.pipelined();
        pipelined.eval(LUA, 1, KNIGHT_ONE);
        pipelined.eval(LUA, 1, KNIGHT_TWO);
        List<Object> objects = pipelined.syncAndReturnAll();
    }
}

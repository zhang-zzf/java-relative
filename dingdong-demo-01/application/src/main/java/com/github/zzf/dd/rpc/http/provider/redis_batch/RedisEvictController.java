package com.github.zzf.dd.rpc.http.provider.redis_batch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-29
 */
@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
@Slf4j
public class RedisEvictController {

    final @Qualifier(REDIS_TEMPLATE) RedisTemplate<String, Object> redisTemplate;

    @DeleteMapping("/")
    public List<String> deleteKey(@RequestParam String keyPattern) {
        List<String> ret = new ArrayList<>();
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            ScanOptions scanOptions = ScanOptions.scanOptions().match(keyPattern).build();
            try (Cursor<byte[]> cursor = connection.scan(scanOptions)) {
                while (cursor.hasNext()) { // 删除所有满足条件的 key
                    byte[] key = cursor.next(); // 返回的是单个 key
                    String keyStr = (String) redisTemplate.getKeySerializer().deserialize(key);
                    log.info("scan -> pattern: {}, key: {}", keyPattern, keyStr);
                    connection.del(key);
                    ret.add(keyStr);
                }
            } catch (IOException e) {// ignore
            }
            return null;
        });
        return ret;
    }

    private static final String REDIS_TEMPLATE = "RedisTemplateAutowire_RedisEvictController";

    @Configuration
    public static class RedisTemplateAutowire {

        @Bean(REDIS_TEMPLATE)
        public RedisTemplate<String, Object> userRedisTemplate(RedisTemplate redisTemplate) {
            return redisTemplate;
        }
    }


}

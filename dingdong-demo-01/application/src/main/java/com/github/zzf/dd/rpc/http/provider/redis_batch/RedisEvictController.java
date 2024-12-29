package com.github.zzf.dd.rpc.http.provider.redis_batch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    // 1. 按类型（包括范型）注入；找不到，进行下一步
    // 2. 按名字 redisTemplate 注入；找不到，进行下一步
    // 3. 报错
    final RedisTemplate<String, Object> redisTemplate;
    final RedisTemplate<Object, Object> objectObjectRedisTemplate;
    final RedisTemplate<String, String> stringStringRedisTemplate;
    final StringRedisTemplate stringRedisTemplate;
    final ApplicationContext applicationContext;

    @DeleteMapping("/")
    public List<String> deleteKey(@RequestParam String keyPattern) {
        List<String> ret = new ArrayList<>();
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            ScanOptions scanOptions = ScanOptions.scanOptions().match(keyPattern).build();
            try (Cursor<byte[]> cursor = connection.scan(scanOptions)) {
                while (cursor.hasNext()) { // 遍历所有满足条件的 key
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

    @GetMapping("/same/template")
    public boolean sameRedisTemplate() {
        Map<String, RedisTemplate> beansOfType = applicationContext.getBeansOfType(RedisTemplate.class);
        // RedisTemplate 没有重写 hashCode，hashCode 代表对象的地址
        // true
        return Objects.hashCode(redisTemplate) == Objects.hashCode(objectObjectRedisTemplate)
            // false
            // && Objects.hashCode(objectObjectRedisTemplate) == Objects.hashCode(stringStringRedisTemplate)
            // true
            && Objects.hashCode(stringStringRedisTemplate) == Objects.hashCode(stringRedisTemplate);
    }

}

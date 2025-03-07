package com.github.zzf.learn.app.repo.redis.id.generator;

import com.github.zzf.learn.app.common.id.generator.IdGeneratorService;
import com.github.zzf.learn.app.common.id.generator.TimeBasedSnowFlake;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeBasedSnowFlakeService implements IdGeneratorService {

    public static final Duration TIMEOUT = Duration.ofMinutes(5);
    private final StringRedisTemplate redisTemplate;

    @Override
    public long id() {
        return uniqueId();
    }

    /**
     * 获取全局唯一 ID
     */
    public long uniqueId() {
        while (true) {
            long id = TimeBasedSnowFlake.generate();
            // expire = 5 minutes
            Boolean success = redisTemplate.opsForValue().setIfAbsent("uid:" + id, "", TIMEOUT);
            if (Boolean.TRUE.equals(success)) { // id 不存在
                return id;
            }
            Thread.yield(); // 稍等后重试
        }
    }

}

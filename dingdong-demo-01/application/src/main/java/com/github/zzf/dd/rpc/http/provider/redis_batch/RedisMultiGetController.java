package com.github.zzf.dd.rpc.http.provider.redis_batch;

import com.github.zzf.dd.redis_multi_get.SomeMultiGetService;
import com.github.zzf.dd.repo.redis.SomeMultiGetServiceCacheImpl;
import com.github.zzf.dd.user.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/redis-multi-get")
public class RedisMultiGetController {

    final SomeMultiGetService someMultiGetService;
    final SomeMultiGetServiceCacheImpl someMultiGetServiceCache;

    @GetMapping("/{area}/users/_search")
    public List<User> batchGet(
        @PathVariable String area,
        @RequestParam List<String> userNoList
    ) {
        return someMultiGetService.batchGetBy(area, userNoList);
    }

    /**
     * 批量清楚缓存
     *
     * @param area
     */
    @DeleteMapping("/{area}/users/cache")
    public void batchDelete(@PathVariable String area) {
        someMultiGetServiceCache.batchEvictCacheByArea(area);
    }


    @GetMapping("/{area}/users/{type}/{username}")
    public User get(@PathVariable String area, @PathVariable String type, @PathVariable String username) {
        return someMultiGetService.getBy(area, User.from(type, username).getUserNo());
    }


}

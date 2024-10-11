package com.github.zzf.actuator.rpc.http.provider.direct_memory;

import static com.github.zzf.actuator.utils.LogUtils.json;

import com.github.zzf.actuator.common.id.generator.TimeBasedSnowFlake;
import com.github.zzf.actuator.rpc.http.provider.direct_memory.dto.BufferCreateReq;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import jakarta.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/netty/direct-memory")
public class NettyDirectMemoryController {

    final Map<Long, ByteBuf> bufferMap = new ConcurrentHashMap<>();

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("java.nio.Bits.MAX_MEMORY", PlatformDependent.estimateMaxDirectMemory());
        info.put("PlatformDependent#hasUnsafe", PlatformDependent.hasUnsafe());
        info.put("PlatformDependent#directBufferPreferred", PlatformDependent.directBufferPreferred());
        // NoCleaner
        info.put("PlatformDependent#useDirectBufferNoCleaner", PlatformDependent.useDirectBufferNoCleaner());
        info.put("-Dio.netty.maxDirectMemory", SystemPropertyUtil.get("io.netty.maxDirectMemory", "null"));
        // NoCleaner 最大内存
        info.put("PlatformDependent#maxDirectMemory", PlatformDependent.maxDirectMemory());
        return info;
    }

    @PostMapping("/unpooled")
    @ResponseStatus(HttpStatus.CREATED)
    public long createUnpooledByteBuffer(@NotNull @RequestBody BufferCreateReq req) {
        log.info("createUnpooledByteBuffer req -> {}", json(req));
        long id = TimeBasedSnowFlake.generate();
        bufferMap.put(id, Unpooled.directBuffer(req.getSize()));
        return req.getSize();
    }

    @PostMapping("/pooled")
    @ResponseStatus(HttpStatus.CREATED)
    public long createPooledByteBuffer(@NotNull @RequestBody BufferCreateReq req) {
        log.info("createPooledByteBuffer req -> {}", json(req));
        long id = TimeBasedSnowFlake.generate();
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(req.getSize());
        buf.writeZero(buf.writableBytes());
        bufferMap.put(id, buf);
        return req.getSize();
    }


    @GetMapping("/")
    public Map<Long, Integer> getByteBufferMap() {
        log.info("getByteBufferMap req");
        return bufferMap.entrySet().stream()
            .collect(Collectors.toMap(Entry::getKey, e -> e.getValue().capacity()));
    }

    @PostMapping("/_clear")
    public void clearByteBuffer(@RequestParam(required = false) Long id) {
        log.info("clearByteBuffer req -> {}", id);
        if (id == null) {
            bufferMap.clear();
        }
        else {
            bufferMap.remove(id);
        }
        System.gc();
    }

}
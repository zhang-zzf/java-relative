package com.github.zzf.dd.rpc.http.provider.direct_memory;


import static com.github.zzf.dd.utils.LogUtils.json;

import com.github.zzf.dd.rpc.http.provider.direct_memory.dto.BufferCreateReq;
import com.github.zzf.dd.common.id.generator.TimeBasedSnowFlake;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
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

    @PostMapping("/unpooled")
    @ResponseStatus(HttpStatus.CREATED)
    public long createUnpooledByteBuffer(@NotNull @RequestBody BufferCreateReq req) {
        log.info("createUnpooledByteBuffer req -> {}", json(req));
        for (int i = 0; i < req.getCount(); i++) {
            long id = TimeBasedSnowFlake.generate();
            bufferMap.put(id, Unpooled.directBuffer(req.getCapacity()));
        }
        return (long) req.getCount() * req.getCapacity();
    }

    @PostMapping("/pooled")
    @ResponseStatus(HttpStatus.CREATED)
    public long createPooledByteBuffer(@NotNull @RequestBody BufferCreateReq req) {
        log.info("createPooledByteBuffer req -> {}", json(req));
        long id = TimeBasedSnowFlake.generate();
        for (int i = 0; i < req.getCount(); i++) {
            ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(req.getCapacity());
            buf.writeZero(buf.writableBytes());
            bufferMap.put(id, buf);
        }
        return (long) req.getCount() * req.getCapacity();
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
    }

    @PostMapping("/_gc")
    public void gc() {
        System.gc();
    }

}
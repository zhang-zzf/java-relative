package com.github.zzf.actuator.rpc.http.provider.direct_memory;

import static com.github.zzf.actuator.utils.LogUtils.json;

import com.github.zzf.actuator.common.id.generator.TimeBasedSnowFlake;
import com.github.zzf.actuator.rpc.http.provider.direct_memory.dto.BufferCreateReq;
import jakarta.validation.constraints.NotNull;
import java.nio.ByteBuffer;
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
@RequestMapping("/api/direct-memory")
public class DirectMemoryController {

    final Map<Long, ByteBuffer> bufferMap = new ConcurrentHashMap<>();

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public long createByteBuffer(@NotNull @RequestBody BufferCreateReq req) {
        log.info("createByteBuffer req -> {}", json(req));
        long id = TimeBasedSnowFlake.generate();
        bufferMap.put(id, ByteBuffer.allocateDirect(req.getSize()));
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
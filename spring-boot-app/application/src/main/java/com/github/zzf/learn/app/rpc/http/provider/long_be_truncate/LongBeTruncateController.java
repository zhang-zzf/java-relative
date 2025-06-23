package com.github.zzf.learn.app.rpc.http.provider.long_be_truncate;

import com.github.zzf.learn.app.common.id.generator.TimeBasedSnowFlake;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-05-25
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/test/long/truncate")
public class LongBeTruncateController {

    @GetMapping
    public LongObject truncate() {
        long generate = TimeBasedSnowFlake.generate();
        return new LongObject().setId(generate).setIdStr(String.valueOf(generate));
    }

    @Data
    public class LongObject {
        Long id;
        String idStr;
    }
}

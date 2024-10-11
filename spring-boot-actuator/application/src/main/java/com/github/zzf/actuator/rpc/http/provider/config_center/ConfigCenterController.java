package com.github.zzf.actuator.rpc.http.provider.config_center;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-10-11
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/config")
public class ConfigCenterController {
    final Environment environment;

    @GetMapping("/{name}")
    public String getConfigByName(@PathVariable String name) {
        log.info("getConfigByName -> key: {}", name);
        return environment.getProperty(name);
    }

}

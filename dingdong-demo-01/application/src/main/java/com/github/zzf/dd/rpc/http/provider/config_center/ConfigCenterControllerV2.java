package com.github.zzf.dd.rpc.http.provider.config_center;

import com.github.zzf.dd.common.Migrate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/config")
public class ConfigCenterControllerV2 implements Migrate {
    final Environment environment;

    // @GetMapping("/{name}")
    public String getConfigByName(@PathVariable String name) {
        log.info("getConfigByName -> key: {}", name);
        return environment.getProperty(name);
    }

}

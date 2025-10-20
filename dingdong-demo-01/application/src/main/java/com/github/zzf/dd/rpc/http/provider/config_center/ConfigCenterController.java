package com.github.zzf.dd.rpc.http.provider.config_center;

import com.github.zzf.dd.common.ConfigService;
import com.github.zzf.dd.common.Migrate;
import com.github.zzf.dd.config.spring.async.SpringAsyncConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-10-11
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/config")
public class ConfigCenterController implements Migrate {
    final Environment environment;
    final ConfigCenterControllerV2 v2;
    final ConfigService config;
    @Qualifier(SpringAsyncConfig.ASYNC_THREAD)
    final Executor executor;

    @GetMapping("/{name}")
    public String getConfigByNameMigrate(@PathVariable String name) {
        return migrate(
                () -> getConfigByName(name),
                () -> v2.getConfigByName(name),
                config.switchOn("ConfigCenterController.getConfigByName.v2", false),
                config.switchOn("ConfigCenterController.getConfigByName.check", false),
                executor
        );
    }

    // @GetMapping("/{name}")
    public String getConfigByName(@PathVariable String name) {
        log.info("getConfigByName -> key: {}", name);
        return environment.getProperty(name);
    }

}

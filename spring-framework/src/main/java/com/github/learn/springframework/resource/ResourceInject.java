package com.github.learn.springframework.resource;

import java.nio.charset.Charset;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResourceInject {
    final Resource yamlFile;
    final Resource[] yamlFileList;

    public ResourceInject(
        @Value("classpath:application.yaml") Resource yamlFile,
        @Value("classpath*:application.yaml") Resource[] yamlFileList) {
        this.yamlFile = yamlFile;
        this.yamlFileList = yamlFileList;
    }

    @SneakyThrows
    public void loadApplicationYaml() {
        String content = yamlFile.getContentAsString(Charset.defaultCharset());
        log.info("application.yaml -> {}", content);
    }
}

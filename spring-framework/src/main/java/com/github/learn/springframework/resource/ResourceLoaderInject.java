package com.github.learn.springframework.resource;

import java.nio.charset.Charset;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceLoaderInject {
  final ResourceLoader resourceLoader;
  final ResourcePatternResolver resourcePatternResolver;

  @SneakyThrows
  public void loadApplicationYaml() {
    String yamlFile = "application.yaml";
    Resource r = resourceLoader.getResource("classpath:" + yamlFile);
    String content = r.getContentAsString(Charset.defaultCharset());
    log.info("{} -> {}", yamlFile, content);
    Resource[] resourceList = resourcePatternResolver.getResources(
        "classpath*:" + yamlFile);
    log.info("resourceList -> {}", resourceList);
  }
}

package com.github.learn.springframework.resource;

import static org.assertj.core.api.BDDAssertions.then;

import java.nio.charset.Charset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

class ResourceCasesTest {

  /**
   * "/application.yaml" -> classpath:/application.yaml
   */
  @SneakyThrows
  @Test
  void givenApplicationContext_whenGetResource_then() {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    // classpath
    Resource resource = ctx.getResource("/application.yaml");
    String content = resource.getContentAsString(Charset.defaultCharset());
    then(content).isNotNull();
  }

  /**
   * "classpath:application.yaml" -> classpath:/application.yaml
   */
  @SneakyThrows
  @Test
  void givenApplicationContext_whenGetClasspathResource_then() {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    // classpath
    Resource resource = ctx.getResource("classpath:application.yaml");
    String content = resource.getContentAsString(Charset.defaultCharset());
    then(content).isNotNull();
  }

  /**
   * <pre>
   *   1. 拷贝一份 application.yaml 到/tmp/resourceA/
   *   2. idea 启动时 添加 -cp /tmp/resourceA
   *   3. "classpath*:application.yaml" -> 同时获取到2份 Resource
   * </pre>
   */
  @SneakyThrows
  @Test
  @Disabled
  void givenApplicationContext_whenGetClasspathResourceList_then() {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    // classpath
    Resource[] resource = ctx.getResources("classpath*:application.yaml");
    then(resource).hasSize(2);
  }

  /**
   * "file://" -> FileUrlResource
   */
  @SneakyThrows
  @Test
  void givenApplicationContext_whenGetFileSystemResource_then() {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    // 获取 IoC 的 Env
    Environment env = ctx.getEnvironment();
    String userDir = env.getProperty("user.dir");
    // classpath
    String loc = "file://" + userDir + "/target/classes/application.yaml";
    Resource resource = ctx.getResource(loc);
    then(resource).isInstanceOf(UrlResource.class);
    String content = resource.getContentAsString(Charset.defaultCharset());
    then(content).isNotNull();
  }

  /**
   * "https://" -> UrlResource
   */
  @SneakyThrows
  @Test
  void givenApplicationContext_whenGetUrlResource_then() {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    // classpath
    Resource resource = ctx.getResource("https://www.baidu.com");
    then(resource).isInstanceOf(UrlResource.class);
    String content = resource.getContentAsString(Charset.defaultCharset());
    then(content).isNotNull();
  }

}

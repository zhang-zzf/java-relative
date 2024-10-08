# README.md

## actuator

### 用途

1. 监控应用 <https://docs.spring.io/spring-boot/reference/actuator/endpoints.html>
    1. health,info,env,caches,scheduledtasks,mappings,metrics,loggers
    1. prometheus
       > 依赖 web  
       > Requires a dependency on micrometer-registry-prometheus  
       > 默认暴露url: /actuator/prometheus
1. 控制应用
    1. loggers,threaddump,shutdown

### 实战 101

1. 添加依赖，参考 application/pom.xml
1. Exposing Endpoints，参考 application/application.yml
1. security 控制，参考 com.github.zzf.actuator.config.ActuatorSecurityConfiguration
1. 

### security/cors

spring 配置 CORS 有2种方式：

- springMVC 原生支持
- CorsFilter
  > jakarta.servlet.Filter to handle CORS pre-flight requests and intercept CORS simple and actual requests with a CorsProcessor, and to update the response, e.g. with CORS response headers, based on the policy matched through the provided CorsConfigurationSource.
  This is an alternative to configuring CORS in the Spring MVC Java config and the Spring MVC XML namespace. It is useful for applications depending only on spring-web (not on spring-webmvc) or for security constraints that require CORS checks to be performed at jakarta.servlet.Filter level.
  This filter could be used in conjunction with DelegatingFilterProxy in order to help with its initialization.

和 spring security 配合时，优先使用 springMVC 方式
> if Spring MVC is on the classpath a HandlerMappingIntrospector is used.  
> You can enable CORS using:

```java

  @Configuration
  @EnableWebSecurity
  public class CorsSecurityConfig {
 
  	@Bean
  	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  		http.cors(withDefaults());
  		return http.build();
  	}
  }
```

参考：
- <https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html#page-title>
- <https://docs.spring.io/spring-framework/reference/web/webmvc-cors.html>

### springweb 添加 日志 filter


## project structure

application 组合 domain / entity / rpc-* 等，作为启动入口。

domain 业务层，收口所有业务逻辑。依赖 entity 提供的业务 model

entity 业务模型层

application.rpc-http 基于 http 的 rpc 实现
> 为 domain 依赖的3方服务提供 consumer 防腐层实现
> 为 domain 暴露的接口提供 provider 实现

application.rpc-dubbo 同 rpc-http

application.repo 仓库层
> 依赖 domain，为 `com.github.zzf.repo.*` 仓库层访问接口提供实现。

```text
miniaa4a @ spring-boot-project git:(master) ✗ ➜ tree -L 2 .
.
├── README.md
├── application
│   ├── pom.xml
│   ├── src
├── domain
│   ├── pom.xml
│   ├── src
├── entity
│   ├── pom.xml
│   ├── src
├── lombok.config
├── pom.xml
```
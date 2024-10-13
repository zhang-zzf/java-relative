# README.md

## netty 内存泄漏检测

### 搞问题

```shell
java --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED
-XX:MaxDirectMemorySize=512M
-Xms256M
-Xmx256M
-Dio.netty.leakDetection.level=PARANOID
```

1. 开启 netty 内存泄漏检测 `-Dio.netty.leakDetection.level=PARANOID`
1. 使用默认的 Cleaner 方案
1. 创建大量 ByteBuf（ByteBuffer） 堆外内存 `com.github.zzf.actuator.rpc.http.provider.direct_memory.NettyDirectMemoryController.createUnpooledByteBuffer`
1. 清除 ByteBuf 引用 `com.github.zzf.actuator.rpc.http.provider.direct_memory.NettyDirectMemoryController.clearByteBuffer`
1. 触发 GC `com.github.zzf.actuator.rpc.http.provider.direct_memory.NettyDirectMemoryController.gc`
1. 检测堆外内存是否被回收（看监控哈）

猜测结论：netty 报告堆外内存泄漏，实际上堆外内存会被回收，GC -> ByteBuffer Cleaner.clean() -> 回收堆外内存  
实验结论：堆外内存会被回收

![img.png](README/img.png)

### Cleaner 策略内存回收

```text
ByteBuf buf = Unpooled.directBuffer(); //1
try {
    // ... do something with buf
    buf.release(); //2
} finally {
    System.gc(); // 触发清理基于 ByteBuffer 的堆外内存
}
```

堆外内存在使用完毕后，推荐及时使用 `buf.release()` 也就是 ByteBuffer.Cleaner.clean() 主动清理堆外内存。

GC 触发的堆外内存回收仅作为兜底方案。

netty 内存泄漏检测：若 ByteBuf 被 GC 回收时没有被 release()，即认为内存泄漏。

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
   > 要求用户有 `ROLE_ENDPOINT_ADMIN`
1. 注册自定义 metric，参考 com.github.zzf.actuator.config.actuator.ActuatorMeterConfiguration

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

参考：
- com.github.zzf.actuator.rpc.http.provider.config.SpringMvcConfig.addInterceptors
  > spring interceptor 只能拦截 spring mvc 请求
- com.github.zzf.actuator.config.log.ServletLogConfiguration
  > servlet filter 实现，可以拦截所有 http 请求

### JWT

JWT 优点：对登录的用户且密码校验合法用户下发 token。后续请求携带 token，不必再次查询 DB 来校验是否是合法用户。
> token 中携带有当前的登录主体（username）及拥有的权限

eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3UFhvRFRaU0VXVTVTNFc4UytJcTR3PT0iLCJhdXRoIjpbIlJPTEVfRU5EUE9JTlRfQURNSU4iLCJST0xFX0FETUlOIl0sImlhdCI6MTcyODM4Mzk3MywibmJmIjoxNzI4MzgzOTczLCJleHAiOjE3NDM5MzU5NzN9.nEEunZZAiCKa3593qPu31Dr36BU3_Ox5ZhAKyph3JFw

```json
{
  "sub": "7PXoDTZSEWU5S4W8S+Iq4w==",
  "auth": [
    "ROLE_ENDPOINT_ADMIN",
    "ROLE_ADMIN"
  ],
  "iat": 1728383973,
  "nbf": 1728383973,
  "exp": 1743935973
}
```

参考: 
- com.github.zzf.actuator.rpc.http.provider.config.security.JWTService
- com.github.zzf.actuator.rpc.http.provider.config.security.JWTAuthenticationFilter
- com.github.zzf.actuator.config.actuator.ActuatorSecurityConfiguration.actuatorSecurityFilterChain
- com.github.zzf.actuator.rpc.http.provider.user.UserController.userToken

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
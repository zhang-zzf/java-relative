# README.md

## 动态更新日志级别

- 配置中心下发

### logback-spring.xml



### spring application.yml

简单配置
- 本地开发环境打印日志到 console
- prod 环境打印日志到文件

```yaml
---
logging:
  level:
    root: info
  pattern:
    dateformat: HH:mm:ss
    #    console: '%d{HH:mm:ss} %-5p [%15.15t] %-40.40logger{39} :[%X{x-trace-id}] %m%n%wEx'
    file: "%d{yyyy-MM-dd'T'HH:mm:ssXXX} %-5p [%15.15t] %-40.40logger{39} :[%X{x-trace-id}] %m%n%wEx"
    console: '%clr(%d{HH:mm:ssXXX}){faint} %clr(${LOG_LEVEL_PATTERN:%5p}) %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %clr([%X{x-trace-id}]) %m%n%wEx'
# %clr(...){faint}  颜色 / %highlight(...) 高亮显示
# %highlight()：颜色，info为蓝色，warn为浅红，error为加粗红，debug为黑色
# %d{yyyy-MM-dd'T'HH:mm:ssXXX} 时间格式
# %-5p 显示日志级别ERROR，WARN，INFO，DEBUG，TRACE；%5若字符长度小于5，则右边用空格填充，%-5若字符长度小于5，则左边用空格填充
# %15.15t 若字符长度小于5，则右边用空格填充；若字符长度超过15，截去多余字符
# %-40.40logger{39}  logger
# %X{x-trace-id} MDC 中的 `x-trace-id`

---
# prod config
spring:
  config:
    activate:
      on-profile: [ prod ]

logging:
  level:
    root: info
  file:
    name: logs/info.log
  logback:
    rollingpolicy:
      max-file-size: 32MB
      total-size-cap: 8192MB
      max-history: 256
  pattern:
    dateformat: MM-dd'T'HH:mm:ssXXX
```

## Distributed Configuration with Consul

[consul 做配配置中心]( https://cloud.spring.io/spring-cloud-consul/reference/html/)

1. 配置变更可追溯（git)，可审批(git pr)，权限管控（git 读写权限）
2. 启动拉取配置 放入 spring Environment
3. 配置变更， app 会被通知
    1. 直接更新到 Spring Environment
    2. 执行变更回调事件
4. 动态/实时/不停服务更改集群配置

### How

1. application.yaml 添加以下配置

    ```yaml
    spring:
      application:
        name: spring-consul-app
      config:
        # To change the connection properties of Consul Config either set spring.cloud.consul.host and spring.cloud.consul.port or add the host/port pair to the spring.config.import statement such as, spring.config.import=optional:consul:myhost:8500. The location in the import property has precedence over the host and port propertie.
        # import: 'optional:consul:'
        # Removing the optional: prefix will cause Consul Config to fail if it is unable to connect to Consul
        import: 'optional:consul:'
      cloud:
        consul:
          host: 127.0.0.1
          port: 8500
          config:
            enabled: true # default
            prefixes: [config] # consul用于存储配置的文件夹根目录名为config
            # sets the folder name used by all applications
            # /config/application/data 中的配置可以配所有服务读取
            default-context: application # default
            profile-separator: '::' # default
            # format: key_value
            format: yaml
            data-key: data # default
            watch:
              enabled: true
    ```

1. consul 配置 KV `/config/application/data` 配置 yaml 格式的

    ```yaml
    threads:
      app: www
      obj: 
        a: true
        b: 1
    spring-@Async-demo: '{"core":8, "max":16, "keepAliveSeconds":60}'
    ```

1. 配置生效规则

    ```text
    # 假设 consul 有以下配置。数字越小，配置优先级越高：
    # 1. config/spring-consul-app::dev/data
    # 2. config/spring-consul-app/data
    # 3. config/application::dev/data
    # 4. config/application/data
    # 指定 `-Dspring.profiles.active=dev` 加载 1,2,3,4 配置
    # 启动没有指定 profile，加载 2,4 配置
    ```

### consul 动态设置线程池大小

参考 SpringAsyncThreadDynamicChanger

1. 启动时从 Env 获取配置，配置线程池
2. consul 配置变更后，监听 Env 变动消息，配置线程池

## actuator

### 用途

参考 actuator-requests.http

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
1. security 控制，参考 com.github.zzf.learn.config.ActuatorSecurityConfiguration
   > 要求用户有 `ROLE_ENDPOINT_ADMIN`
1. 注册自定义 metric，参考 com.github.zzf.learn.config.actuator.ActuatorMeterConfiguration

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
- com.github.zzf.learn.rpc.http.provider.config.SpringMvcConfig.addInterceptors
  > spring interceptor 只能拦截 spring mvc 请求
- com.github.zzf.learn.config.log.ServletLogConfiguration
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
- com.github.zzf.learn.rpc.http.provider.config.security.JWTService
- com.github.zzf.learn.rpc.http.provider.config.security.JWTAuthenticationFilter
- com.github.zzf.learn.config.actuator.ActuatorSecurityConfiguration.actuatorSecurityFilterChain
- com.github.zzf.learn.rpc.http.provider.user.UserController.userToken

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
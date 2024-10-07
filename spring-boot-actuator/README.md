# README.md

## actuator

### 用途

1. 监控应用 <https://docs.spring.io/spring-boot/reference/actuator/endpoints.html>
    1. health,info,env,caches,scheduledtasks,mappings,metrics,loggers
    1. prometheus
       > 依赖 web  
       > Requires a dependency on micrometer-registry-prometheus
1. 控制应用
    1. loggers,threaddump,shutdown

### 实战 101

1. 添加依赖，参考 application/pom.xml
1. Exposing Endpoints，参考 application/application.yml
1. security 控制

### security

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
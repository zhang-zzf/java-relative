# README.md

## project structure

application 组合 domain / entity / rpc-* 等，作为启动入口。
> application 不添加任何类

domain 业务层，收口所有业务逻辑。依赖 entity 提供业务 model

entity 业务模型层

rpc-http 基于 http 的 rpc 实现
> 依赖 domain，为 domain 依赖的3方服务提供 consumer 实现
> 依赖 domain，为 domain 暴露的接口提供 provider 实现

rpc-dubbo 同 rpc-http

repository 仓库层
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
├── repo
│   ├── pom.xml
│   ├── src
├── lombok.config
├── pom.xml
├── rpc-dubbo
│   ├── pom.xml
│   ├── src
└── rpc-http
    ├── pom.xml
    └── src
```
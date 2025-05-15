# README.md

## 202504

### springboot actuator 配置打点

推荐优先使用 spring-boot application.yaml 配置文件的方式配置 Histogram / Summary 的 bucket / percentiles。

```yaml
management:
  metrics:
    tags:
      application: ${spring.application.name}
    distribution:
      slo: # 按 metric 名字维度，配置直方桶 bucket
        ActuatorAutoController.timed: 1ms, 5ms
        http.server.requests: 10ms, 50ms, 100ms
      percentiles:
        all: 0.95, 0.99 # 配置默认百分比
        http.server.requests: 0.95 # 针对单个 metric 配置
```

#### @Timed

默认全部都统计到 `method_timed` 指标下，只用 `class` / `method` 来区分不同的方法

```text
# 指标分析
# # TYPE method_timed_seconds summary
	  # 1个 count 统计的次数
	  # 1个 sum 统计值的总和
# TYPE method_timed_seconds_max gauge
		# 1个 max 记录最大值
		
# 默认配置只能计算 avg 哈
```

```text
# HELP method_timed_seconds  
# TYPE method_timed_seconds summary
method_timed_seconds_count{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.direct_memory.DirectMemoryController",exception="none",method="getByteBufferMap",} 1.0
method_timed_seconds_sum{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.direct_memory.DirectMemoryController",exception="none",method="getByteBufferMap",} 1.477970834
# HELP method_timed_seconds_max  
# TYPE method_timed_seconds_max gauge
method_timed_seconds_max{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.direct_memory.DirectMemoryController",exception="none",method="getByteBufferMap",} 1.477970834
```

- 如何自定义指标名字
  - 如何自定义 Percentiles / bucket


```text
自定义指标名字和 percentiles
@Timed(value = "ActuatorAutoController.timed", percentiles = {0.5, 0.9, 0.95, 0.99}, extraTags = {"tag1", "time1"})

# 指标分析
# # ActuatorAutoController_timed summary
	  # 4个 quantile 计算好的百分比
	  # 1个 count 统计的次数
	  # 1个 sum 统计值的总和
# ActuatorAutoController_timed gauge
		# 1个 max 记录最大值
		
# HELP ActuatorAutoController_timed_seconds  
# TYPE ActuatorAutoController_timed_seconds summary
ActuatorAutoController_timed_seconds{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",quantile="0.5",} 6.5536E-5
ActuatorAutoController_timed_seconds{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",quantile="0.9",} 0.005500928
ActuatorAutoController_timed_seconds{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",quantile="0.95",} 0.005500928
ActuatorAutoController_timed_seconds{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",quantile="0.99",} 0.005500928
ActuatorAutoController_timed_seconds_count{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",} 2.0
ActuatorAutoController_timed_seconds_sum{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",} 0.00552125
# HELP ActuatorAutoController_timed_seconds_max  
# TYPE ActuatorAutoController_timed_seconds_max gauge
ActuatorAutoController_timed_seconds_max{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",} 0.005455
```

```text
自定义指标名字和 percentiles
@Timed(value = "ActuatorAutoController.timed", percentiles = {0.5, 0.9, 0.95, 0.99}, extraTags = {"tag1", "time1"})

application.yaml 中自定义 bucket 
management:
  metrics:
    tags:
      application: ${spring.application.name}
    distribution:
      slo:
        ActuatorAutoController.timed: 1ms, 5ms
        ActuatorAutoController.timed2: 1ms, 5ms, 10ms

# 指标分析
# # ActuatorAutoController_timed histogram
		# 4个 quantile 计算好的百分比
		# 2个 bucket 分布桶
	  # 1个 count 统计的次数
	  # 1个 sum 统计值的总和
# ActuatorAutoController_timed gauge
		# 1个 max 记录最大值

# HELP ActuatorAutoController_timed_seconds  
# TYPE ActuatorAutoController_timed_seconds histogram
ActuatorAutoController_timed_seconds{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",quantile="0.5",} 7.168E-5
ActuatorAutoController_timed_seconds{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",quantile="0.9",} 3.25632E-4
ActuatorAutoController_timed_seconds{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",quantile="0.95",} 0.008124416
ActuatorAutoController_timed_seconds{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",quantile="0.99",} 0.008124416
ActuatorAutoController_timed_seconds_bucket{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",le="0.001",} 9.0
ActuatorAutoController_timed_seconds_bucket{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",le="0.005",} 9.0
ActuatorAutoController_timed_seconds_bucket{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",le="+Inf",} 10.0
ActuatorAutoController_timed_seconds_count{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",} 10.0
ActuatorAutoController_timed_seconds_sum{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",} 0.008821459

# HELP ActuatorAutoController_timed_seconds_max  
# TYPE ActuatorAutoController_timed_seconds_max gauge
ActuatorAutoController_timed_seconds_max{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.micrometer.ActuatorAutoController",exception="none",method="timed",tag1="time1",} 0.007896542
```

#### @Counted

默认全部都统计到 `method_counted` 指标下，只用 `class` / `method` 来区分不同的方法

```text
# 指标分析
# TYPE method_counted_total counter
	  # 1个 count 统计的的值的和（QPS 的值是1）
```

```text
# HELP method_counted_total  
# TYPE method_counted_total counter
method_counted_total{application="spring-boot-app",class="com.github.zzf.learn.app.rpc.http.provider.direct_memory.DirectMemoryController",exception="none",method="clearByteBuffer",result="success",} 2.0
```

#### 自定义 timer 同时指定 bucket 和 percentiles

```java
Timer.builder(metricName)
                .tags(tags)
                // .publishPercentileHistogram()
                // .minimumExpectedValue(Duration.ofNanos(1))
                // .maximumExpectedValue(Duration.ofNanos(64))
                .serviceLevelObjectives(Duration.ofNanos(1000), Duration.ofNanos(2000))
                .publishPercentiles(0.5, 0.9, 0.95, 0.99)
                .register(Metrics.globalRegistry)
```

```text
# 指标分析
# TYPE ActuatorAutoController_nanoTimer_seconds histogram
		# 4个 quantile 计算好的百分比
	  # 3个 bucket 直方图
	  # 1个 count 统计的次数
	  # 1个 sum 统计值的总和
# TYPE ActuatorAutoController_nanoTimer_seconds_max gauge
```

```text
# HELP ActuatorAutoController_nanoTimer_seconds  
# TYPE ActuatorAutoController_nanoTimer_seconds histogram
ActuatorAutoController_nanoTimer_seconds{application="spring-boot-app",quantile="0.5",} 5.2E-8
ActuatorAutoController_nanoTimer_seconds{application="spring-boot-app",quantile="0.9",} 6.7E-7
ActuatorAutoController_nanoTimer_seconds{application="spring-boot-app",quantile="0.95",} 6.7E-7
ActuatorAutoController_nanoTimer_seconds{application="spring-boot-app",quantile="0.99",} 6.7E-7
ActuatorAutoController_nanoTimer_seconds_bucket{application="spring-boot-app",le="1.0E-6",} 2.0
ActuatorAutoController_nanoTimer_seconds_bucket{application="spring-boot-app",le="2.0E-6",} 2.0
ActuatorAutoController_nanoTimer_seconds_bucket{application="spring-boot-app",le="+Inf",} 2.0
ActuatorAutoController_nanoTimer_seconds_count{application="spring-boot-app",} 2.0
ActuatorAutoController_nanoTimer_seconds_sum{application="spring-boot-app",} 6.92E-7
# HELP ActuatorAutoController_nanoTimer_seconds_max  
# TYPE ActuatorAutoController_nanoTimer_seconds_max gauge
ActuatorAutoController_nanoTimer_seconds_max{application="spring-boot-app",} 6.4E-7
```

## 202503

### idea mybatis + mybatis-plus 快速开发

idea MybatisCodeHelperPro 根据表快速生成 entity.java / mapper.java / mapper.xml

1. 打开 idea 的 Database，连接到 DEV 数据库
1. 在 Database 中右击需要生成代码的表，选择 `Mybatis generator`
1. 调整下生成配置，点击 `OK`

### app TE/PROD 启动步骤

#### idea 启动项目

1. 通过 env/*.sh 把项目需要的资源(MySQL / Redis / Consul 等) 通过 ssh 本地代理端口映射到本地
1. 项目 `application.yaml` 中添加配置
1. 直接在 idea debug Application 即可

#### TE / PROD 启动步骤

```text
u2004020 ➜  spring-boot-app tree .
.
├── application-1.0-SNAPSHOT.jar
├── application-prod.yaml
├── logs
│   └── app.log
└── nohup.out
```

1. 在项目 jar 包的同目录下放置 `application-prod.yaml` , 配置 prod 环境需要的资源地址等配置
1. 指定 spring profile `-Dspring.profiles.active=prod` 启动
    > `nohup java -server -Xmx256m -Xms256m -XX:MaxDirectMemorySize=4g -XX:+UseZGC -Dspring.profiles.active=prod -jar application-1.0-SNAPSHOT.jar &>nohup.out &`  
    >
    > `application-prod.yaml` 会自动覆盖项目中 `application.yaml` 中的同名配置

### spring-boot 项目配置原则

1. 代码中的 `application.yaml` 配置可以让项目在本地环境中启动
1. 生产环境的配置**不能**放在代码中

#### iot-card 项目配置方案整理

1. 代码中的 `application.yaml` 存放 dev 配置及 prod 非机密配置
1. `application-wx-pay.yaml` 及 `application-wx-mp.yaml` 只是配置参考模版，并无 prod 环境配置
1. prod 环境中存在 `application-prod.yaml` 配置文件
   > `nohup java -server -Xms256m -Xmx256m -Xmn128M -Dspring.profiles.active=prod -jar app.jar &> /dev/null &`
   >
   > prod 项目启动时指定 profile
   >
   > 注意: spring 将同时加载 `application-prod.yaml` 和 `application.yaml` 中的配置。`application-prod.yaml` 将覆盖 `application.yaml` 中的同名配置

方案总结：

1. 本地环境(`application.yaml`)存在启动的全量配置，可以直接启动
1. TE / prod 环境存在和环境相关的配置文件，启动时指定 `-Dspring.profiles.active=prod` 额外加载和环境相关的配置

#### 本地使用 prod 配置

线上环境配置不能存放在 code 项目中。使用 idea 启动配置的方式，额外添加 `-Dspring.application.json` 配置。

```text
-Xms384M
-Xmx384M
-Dspring.application.json={\"wx\":{\"mp\":{\"enabled\":true,\"checkSignature\":true,\"configs\":[{\"appId\":\"wx06ae7a55f5083fa0\",\"secret\":\"17daabdbbee106ad016bba3dce5fd192\",\"token\":\"oKuHHi00aZGTmbfp4t6p98OXdOwiC9T\",\"aesKey\":\"171mMpWutCRTJ99ssHbJuRCvzSEDZ01QlY728bXupie\",\"oauth2redirectUri\":\"https://mp-wx.iot.chuanshi.ltd/\"}]}}}
```

![idea 启动项目配置文件](README/2025-03-10-17-35-04.png)

### micrometer 实战

参考项目： mqtt / iot-card

### spring-boot-actuator 能力

参考: [actuator-requests.http](.)

1. `/actuator/info` host/jvm info
1. `/actuator/health` 健康检查
1. `/actuator/prometheus` prometheus exporter
1. `/actuator/metrics`
1. `/actuator/caches`
1. `/actuator/env`
1. `/actuator/loggers`
    > query / update logger level
1. `/actuator/scheduledtasks`
1. `/actuator/mappings`
    > url mapping

#### actuator 监控能力

For metrics and traces, Spring Boot uses Micrometer Observation.

参考:

- [ActuatorMeterConfiguration](com.github.zzf.learn.app.config.actuator.ActuatorMeterConfiguration)
- [Supported Metrics and Meters](https://docs.spring.io/spring-boot/reference/actuator/metrics.html#actuator.metrics.supported)

#### actuator + prometheus + grafana + consul 监控能力

1. spring-boot-actuator 提供基础监控指标
1. spring-boot 启动时注册服务到 consul

    ```yaml
    spring:
      application:
        name: spring-boot-app
      config:
        import: 'optional:consul:'
      cloud:
        consul:
          discovery:
            instanceId: ${spring.application.name}:${server.port} # 单机启动多个服务，以 server.port 区分
            tags: ['spring-boot', 'actuator']
            metadata:
              author: zhang.zzf
              env: dev
              cluster: spring-boot-app
            health-check-timeout: 1s
            healthCheckCriticalTimeout: 1m
    ```

1. prometheus.yaml 配置收集服务的监控指标

```yaml
scrape_configs:
  - job_name: "spring-actuator"
    metrics_path: "/actuator/prometheus"
    # 从 consul 中拉取所有包含 `.*spring-boot.*` consul tag 的服务
    consul_sd_configs:
    - server: '192.168.56.20:8500'
      services: []
    relabel_configs:
    - source_labels: [__meta_consul_tags]
      regex: '.*spring-boot.*'
      action: keep
```

1. grafana 展示
   1. JVM (Micrometer)
      1. JVM 相关 HEAP / GC / Threads
      1. JVM 进程内存 / CPU / LOAD / 进程打开的文件
   1. Spring Boot Actuator
      1. HTTP 请求监控
      1. 线程池
      1. Basic  CPU / LOAD / 进程打开的文件
      1. HikariCP 链接池
      1. 日志

### mybatis 打印日志

#### 方案1

`application.yaml` 文件中把 mapper 接口所在的 package 的日志打印级别配置为 debug
> 可以配置多个包哈

```yaml
logging:
  level:
    root: info
    # mapper 接口所在的 package
    com.github.zzf.learn.app.repo.mysql.db0.mapper: debug
```

#### 方案2（优先使用方案1）

1. 在自定义的 MybatisSqlSessionFactoryBean 中添加配置

    ```java
        /**
         * 定义mybatis
         */
        @Bean(SQL_SESSION_FACTORY_BEAN_ID)
        public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(
            @Qualifier(DATASOURCE_BEAN_ID) DataSource dataSource) {
            MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
            factory.setConfiguration(myCustomConfiguration());
            factory.setDataSource(dataSource);
            factory.setVfs(SpringBootVFS.class);
            return factory;
        }

        private MybatisConfiguration myCustomConfiguration() {
            MybatisConfiguration c = new MybatisConfiguration();
            c.setLogPrefix("mybatis.sql.");
            return c;
        }
    ```

1. application.yaml 中添加日志配置

    ```yaml
    logging:
      level:
        root: info
        mybatis.sql: debug
    ```

### DDD maven 项目命名

项目服务名为: `spring-boot-app`, 项目目录结构如下

```text
mini243b ➜  spring-boot-app git:(master*)tree -L 2   
.
├── README.md
├── application
│   ├── pom.xml
├── domain
│   ├── pom.xml
├── entity
│   ├── pom.xml
└── pom.xml
```

项目 pom.xml 中配置 module 关系如下:

```xml
  <groupId>com.github.zzf.learn.app</groupId>
  <artifactId>spring-boot-app</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>pom</packaging>

  <modules>
    <module>application</module>
    <module>domain</module>
    <!-- module 是目录名字，可以和 entity 的 artifactId 不一致 -->
    <module>entity</module>
  </modules>
```

entity pom.xml 的配置如下:

```xml
  <parent>
    <artifactId>spring-boot-app</artifactId>
    <groupId>com.github.zzf.learn.app</groupId>
    <version>1.0-SNAPSHOT</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <version>${entity.version}</version>
  <artifactId>spring-boot-app-entity</artifactId>
```

关注点:

1. entity 要发布到公司 maven repo，共第三方服务使用。 `artifactId` 必须唯一。
   > maven 使用 groupId + artifactId 来定位 jar 包  
   > entity module 的 artifactId 使用 `${serviceName}-entity` 来区分其他项目的 entity  
   > 代码的 package 使用 `${业务组group}.${项目}` 的方式组织，避免和其他 entity package 重复

   ![package](README/2025-03-09-17-35-04.png)

1. domain module 和 application module 是不能给第三方服务依赖的

## Aop 统一 debug 日志

### controller 入口

### DB mybatis

### feign rpc

## 动态更新日志级别

env: spring-boot + logback

- 配置中心下发

### http 接口

controller 参考: log.provider.http.rpc.com.github.zzf.learn.app.LoggerController

```http
### 获取日志级别
GET {{host}}/loggers
Content-Type: application/json

### 获取日志级别
GET {{host}}/loggers/com.github.zzf.learn.app.rpc.http.provider.log
Content-Type: application/json

### 设置日志级别
PUT {{host}}/loggers/com.github.zzf.learn.app.rpc.http.provider.log
Content-Type: application/json

{ "level": "DEBUG" }

### 设置日志级别
PUT {{host}}/loggers/com.github.zzf.learn.app.rpc.http.provider.log
Content-Type: application/json

{ "level": "info" }
```

### logback-spring.xml

参考 logback-spring-prod.xml

用于定义 prod 环境的日志配置

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

## Consul 默认配置

```yaml
spring:
  application:
    name: spring-boot-app
  config:
    # To change the connection properties of Consul Config either set spring.cloud.consul.host and spring.cloud.consul.port or add the host/port pair to the spring.config.import statement such as, spring.config.import=optional:consul:myhost:8500. The location in the import property has precedence over the host and port propertie.
    # import: 'optional:consul:'
    # Removing the optional: prefix will cause Consul Config to fail if it is unable to connect to Consul
    import: 'optional:consul:localhost:8500'
  cloud:
    consul:
      # enabled: true # default
      # host: localhost # default
      # port: 8500 # default
      discovery:
        # enabled: true # default
        # prefer-ip-address: false # default
        # instanceId: ${spring.application.name}:${vcap.application.instance_id:${spring.application.instance_id:${random.value}}}
        instanceId: ${spring.application.name}:${server.port}
        tags: ['spring-boot', 'actuator']
        metadata:
          author: zhang.zzf
          env: dev
          cluster: spring-boot-app
        # health check
        # register-health-check: true # default
        # health-check-path: "/actuator/health" # default
        # health-check-interval: 10s # default
        health-check-timeout: 1s
        healthCheckCriticalTimeout: 5m
      config:
        enabled: true # default
        prefixes: [config] # consul用于存储配置的文件夹根目录名为config
        # sets the folder name used by all applications
        # /config/application/data 中的配置可以配所有服务读取
        default-context: ${spring.application.name} # default
        profile-separator: '::' # default
        # format: key_value
        format: yaml
        data-key: data # default
        watch:
          enabled: true

# 假设 consul 有以下配置。数字越小，配置优先级越高：
# 1. config/spring-consul-app::dev/data
# 2. config/spring-consul-app/data
# 3. config/application::dev/data
# 4. config/application/data

# 指定 `-Dspring.profiles.active=dev` 加载 1,2,3,4 配置
# 启动没有指定 profile，加载 2,4 配置
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
1. security 控制，参考 com.github.zzf.learn.app.config.ActuatorSecurityConfiguration
   > 要求用户有 `ROLE_ENDPOINT_ADMIN`
1. 注册自定义 metric，参考 actuator.config.com.github.zzf.learn.app.ActuatorMeterConfiguration

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

- config.provider.http.rpc.com.github.zzf.learn.app.SpringMvcConfig.addInterceptors
  > spring interceptor 只能拦截 spring mvc 请求
- servlet.trace.config.com.github.zzf.learn.app.ServletTraceConfiguration
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

- security.config.provider.http.rpc.com.github.zzf.learn.app.JWTService
- security.config.provider.http.rpc.com.github.zzf.learn.app.JWTAuthenticationFilter
- actuator.config.com.github.zzf.learn.app.ActuatorSecurityConfiguration.actuatorSecurityFilterChain
- user.provider.http.rpc.com.github.zzf.learn.app.UserController.userToken

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

# README

## redis json 低效率方案

keyword: `redis` `json` `compress` `msgpack` `gzip` `lz`

思路:

- 压缩 json
- 换其他序列化框架

## 压缩

使用 apache `commons-compress` 框架，统一接口支持多种压缩算法。

```xml
    <dependency>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-compress</artifactId>
      <version>1.27.1</version>
    </dependency>
```

`RedisSerializerCompressWrapper` 使用 `Decorator Pattern` 针对现有的 `RedisSerializer` (如 `GenericJackson2JsonRedisSerializer`) 快速添加字节压缩能力。

- gzip
- lz4

## 序列化框架

- kryo
  - 踩了坑
- msgpack
  - [It's like JSON.but fast and small.](https://msgpack.org/)
  - [jackson-dataformat-msgpack](https://github.com/msgpack/msgpack-java/blob/main/msgpack-jackson/README.md)

### jackson-dataformat-msgpack

Serialize/Deserialize POJO as MessagePack array type to keep compatibility with msgpack-java:0.6
In msgpack-java:0.6 or earlier, a POJO was serliazed and deserialized as an array of values in MessagePack format. The order of values depended on an internal order of Java class's variables and it was a naive way and caused some issues since Java class's variables order isn't guaranteed over Java implementations.

On the other hand, jackson-databind serializes and deserializes a POJO as a key-value object. So this jackson-dataformat-msgpack also handles POJOs in the same way. As a result, it isn't compatible with msgpack-java:0.6 or earlier in serialization and deserialization of POJOs.

But if you want to make this library handle POJOs in the same way as msgpack-java:0.6 or earlier, you can use JsonArrayFormat like this:

```java
  ObjectMapper objectMapper = new MessagePackMapper();
  objectMapper.setAnnotationIntrospector(new JsonArrayFormat());
```

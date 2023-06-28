package com.feng.insure.protocol.insureserver.controller.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/12/6
 */
@Configuration
public class JacksonConfig {

  @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
  private String pattern;

  @Bean
  public LocalDateTimeSerializer localDateTimeSerializer() {
    return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
  }

  @Bean
  public LocalDateTimeDeserializer localDateTimeDeserializer() {
    return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern));
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder -> {
      builder.serializerByType(LocalDateTime.class, localDateTimeSerializer());
      builder.deserializerByType(LocalDateTime.class, localDateTimeDeserializer());
      builder.simpleDateFormat(pattern);
    };
  }

}

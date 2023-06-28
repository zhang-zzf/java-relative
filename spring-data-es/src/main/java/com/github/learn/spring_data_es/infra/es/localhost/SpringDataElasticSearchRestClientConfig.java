package com.github.learn.spring_data_es.infra.es.localhost;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

/**
 * @author zhanfeng.zhang
 * @date 2021/06/04
 */
@Configuration
public class SpringDataElasticSearchRestClientConfig extends AbstractElasticsearchConfiguration {

  @Override
  @Bean
  public RestHighLevelClient elasticsearchClient() {
    ClientConfiguration clientConfiguration = ClientConfiguration.builder()
        .connectedTo("localhost:9200", "localhost:9201")
        .build();
    return RestClients.create(clientConfiguration).rest();
  }


  @Bean
  @Override
  public ElasticsearchCustomConversions elasticsearchCustomConversions() {
    return new ElasticsearchCustomConversions(Arrays.asList(
        LocalDateTimeToString.INSTANCE, StringToLocalDateTime.INSTANCE
    ));
  }

  @ReadingConverter
  enum StringToLocalDateTime implements Converter<String, LocalDateTime> {

    INSTANCE;

    @Override
    public LocalDateTime convert(String source) {
      return LocalDateTime.parse(source);
    }
  }

  @WritingConverter
  enum LocalDateTimeToString implements Converter<LocalDateTime, String> {

    INSTANCE;

    @Override
    public String convert(LocalDateTime source) {
      return source.toString();
    }
  }
}


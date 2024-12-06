package com.github.learn.spring_data_es.infra.es.localhost;

import static java.util.Collections.singletonMap;
import static org.elasticsearch.action.support.WriteRequest.RefreshPolicy.IMMEDIATE;

import java.io.IOException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhanfeng.zhang
 * @date 2021/06/04
 */
@SpringBootTest
class SpringDataElasticSearchRestClientConfigIT {


    @Autowired
    RestHighLevelClient highLevelClient;

    @Test
    public void test() throws IOException {
        IndexRequest request = new IndexRequest("spring-data")
            .id("1")
            .source(singletonMap("feature", "high-level-rest-client"))
            .setRefreshPolicy(IMMEDIATE);
        IndexResponse response = highLevelClient.index(request, RequestOptions.DEFAULT);
    }

}
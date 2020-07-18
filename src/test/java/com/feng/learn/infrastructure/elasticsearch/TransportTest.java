package com.feng.learn.infrastructure.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.feng.learn.infrastructure.elasticsearch.TransportTest.Context;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ConstantScoreQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * @author zhanfeng.zhang
 * @date 2020/06/11
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    Context.class,
})
@Ignore // 环境相关
public class TransportTest {

    @Autowired
    TransportClient transportClient;

    private final String index = "ele_zhongbao";
    private final String type = "knight";

    /**
     * 测试是全量字段更新/还是只更新部分字段
     * <p>字段是部分更新</p>
     * <p>upsert 不存在则插入，id 即为 UpdateRequest 中的id</p>
     */
    @Test
    public void testUpdateOrInsert() throws ExecutionException, InterruptedException {
        final String id = "2106724";
        Map<String, Object> get = transportClient.prepareGet(index, type, id).get().getSource();
        if (get == null) {
            get = new HashMap<>();
        }
        get.remove("optimal");
        get.remove("is_support_long_distance");
        get.put("work_status", 1);
        get.put("delivery_id", id);
        String jsonString = JSON.toJSONString(get);
        UpdateRequest upsert = new UpdateRequest()
            .index(index).type(type).id(id)
            .doc(jsonString, XContentType.JSON)
            // 不存在则插入
            .upsert(jsonString, XContentType.JSON);
        BulkRequest bulkRequest = Requests.bulkRequest().add(upsert);
        BulkResponse bulkItemResponses = transportClient.bulk(bulkRequest).get();
        Map<String, Object> updated = transportClient.prepareGet(index, type, id).get().getSource();
    }

    @Test
    public void testSearchAfter() throws ExecutionException, InterruptedException {
        Long timeInSecond = Instant.now().getEpochSecond();
        Map<Long, Long> ret = new HashMap<>();
        Long searchAfter = 0L;
        String updateTimeField = "update_time";
        while (true) {
            SearchRequest request = Requests.searchRequest(index).source(SearchSourceBuilder.searchSource()
                .fetchSource(updateTimeField, null)
                /* 每次拉取 1W 的数据量*/
                .size(64)
                .sort(updateTimeField, SortOrder.ASC)
                .searchAfter(new Object[]{searchAfter})
                .query(new ConstantScoreQueryBuilder(
                    new BoolQueryBuilder()
                        /* 在线*/
                        .filter(new TermQueryBuilder("work_status", 0))
                        // 最后更新时间小于 timeInSecond
                        .filter(new RangeQueryBuilder(updateTimeField).lte(timeInSecond)))));
            SearchHit[] hits = transportClient.search(request).get().getHits().getHits();
            if (hits.length == 0) {
                break;
            }
            ret.putAll(Stream.of(hits).collect(toMap(
                hit -> new Long(hit.getId()),
                hit -> new Long(hit.getSourceAsMap().get(updateTimeField).toString()))));
            searchAfter = new Long(hits[hits.length - 1].getSourceAsMap().get(updateTimeField).toString());
        }
    }

    @Configuration
    static class Context {

        @Bean
        public TransportClient transportClient() throws UnknownHostException {
            Settings settings = Settings.builder()
                .put("cluster.name", "my-esLearn")  //连接的集群名
                .put("client.transport.ignore_cluster_name", true)  //如果集群名不对，也能连接
                .put("client.transport.sniff", true)//嗅探功能开启
                .build();
            return new PreBuiltTransportClient(settings)
                .addTransportAddress(
                    new InetSocketTransportAddress(InetAddress.getByName("10.103.108.85"),
                        8300));
        }
    }
}

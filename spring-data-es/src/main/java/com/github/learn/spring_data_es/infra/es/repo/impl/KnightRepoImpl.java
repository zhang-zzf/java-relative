package com.github.learn.spring_data_es.infra.es.repo.impl;

import static java.util.stream.Collectors.toList;

import com.github.learn.spring_data_es.domain.model.Knight;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.MultiGetItem;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

/**
 * @author zhanfeng.zhang
 * @date 2021/06/04
 */
@Repository
@RequiredArgsConstructor
public class KnightRepoImpl {

  final ElasticsearchOperations elasticsearchOperations;

  public void index(List<Knight> knightList) {
    final List<IndexQuery> indexQueries = knightList.stream()
        .map(this::convertToQuery)
        .collect(toList());
    elasticsearchOperations.bulkIndex(indexQueries, Knight.class);
  }

  public List<Knight> batchGetById(List<Long> personIdList) {
    final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
        .withIds(personIdList.stream().map(String::valueOf).collect(toList())).build();
    final List<MultiGetItem<Knight>> knights = elasticsearchOperations.multiGet(searchQuery,
        Knight.class);
    return knights.stream().map(MultiGetItem::getItem).collect(toList());
  }

  private IndexQuery convertToQuery(Knight knight) {
    return new IndexQueryBuilder()
        .withId(knight.getPersonId().toString())
        .withObject(knight)
        .build();
  }


}

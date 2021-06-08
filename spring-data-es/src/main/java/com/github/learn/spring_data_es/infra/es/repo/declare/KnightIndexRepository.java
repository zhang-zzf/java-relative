package com.github.learn.spring_data_es.infra.es.repo.declare;

import com.github.learn.spring_data_es.infra.es.entity.Knight;
import java.util.List;
import java.util.Optional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author zhanfeng.zhang
 * @date 2021/06/08
 */
public interface KnightIndexRepository extends ElasticsearchRepository<Knight, Long> {

    /**
     * 按 knightId 查询
     *
     * @param knightId knightId
     * @return Knight
     */
    Optional<Knight> findByAccount_KnightId(Long knightId);

    List<Knight> findByAccount_KnightIdAndAccount_SubId(long knightId, long subId);
}

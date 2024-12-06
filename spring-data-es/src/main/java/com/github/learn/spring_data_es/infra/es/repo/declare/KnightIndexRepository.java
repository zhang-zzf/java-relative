package com.github.learn.spring_data_es.infra.es.repo.declare;

import com.github.learn.spring_data_es.domain.model.Knight;
import java.util.List;
import java.util.Optional;
import org.springframework.data.elasticsearch.annotations.Query;
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

    /**
     * 按 knightId subId 搜索
     *
     * @param knightId knightId
     * @param subId    subId
     * @return Knight
     */
    @Query("{\n"
        + "  \"bool\": {\n"
        + "    \"filter\": [\n"
        + "      {\n"
        + "        \"nested\": {\n"
        + "          \"path\": \"account\",\n"
        + "          \"query\": {\n"
        + "            \"bool\": {\n"
        + "              \"filter\": [\n"
        + "                {\n"
        + "                  \"terms\": {\n"
        + "                    \"account.knightId\": [\n"
        + "                      \"?0\"\n"
        + "                    ]\n"
        + "                  }\n"
        + "                },\n"
        + "                {\n"
        + "                  \"terms\": {\n"
        + "                    \"account.subId\": [\n"
        + "                      \"?1\"\n"
        +
        "                    ]\n"
        + "                  }\n"
        + "                }\n"
        + "              ]\n"
        + "            }\n"
        + "          }\n"
        + "        }\n"
        + "      }\n"
        + "    ]\n"
        + "  "
        + "}\n"
        + "}")
    List<Knight> findByAccount_KnightIdAndAccount_SubId(long knightId, long subId);
}

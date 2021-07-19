package com.github.learn.spring_data_es.domain.model;

import static org.springframework.data.elasticsearch.annotations.DateFormat.basic_date_time_no_millis;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author zhanfeng.zhang
 * @date 2021/06/04
 */
@Data
@Document(indexName = "archive_person", createIndex = false)
@Accessors(chain = true)
public class Knight {

    @Id
    private Long personId;

    @Field(type = FieldType.Nested)
    private Set<Account> account;

    private Register register;

    private Identity identity;

    @Data
    @Accessors(chain = true)
    public static class Account {

        private Long knightId;
        private String subType;
        private Long subId;
        private String mobile;
        private String status;
    }

    @Data
    @Accessors(chain = true)
    public static class Register {

        private Long cityId;
        private String subType;
        private LocalDateTime createdAt;
    }

    @Data
    @Accessors(chain = true)
    public static class Identity {

        private String name;
        private String sex;
        private String ethnic;
        private String city;
        private LocalDateTime birthDay;
    }

}

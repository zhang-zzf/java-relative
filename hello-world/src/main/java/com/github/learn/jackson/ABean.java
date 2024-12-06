package com.github.learn.jackson;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

@Data
@JsonInclude(value = NON_ABSENT, content = NON_ABSENT)
class ABean {

    Integer id;
    /**
     * 序列化和反序列化
     */
    @JsonProperty("userName")
    String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS", timezone = "GMT+8")
    LocalDateTime updatedAt;

}

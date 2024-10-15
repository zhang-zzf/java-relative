package com.github.learn.java_date.jackson;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Deserialization 遇到本类中没有的字段不报错
@JsonInclude(value = NON_NULL, content = NON_NULL) // Serialization 不输出 null 字段
public class Bean4 {

    Long id;
    @JsonProperty("order")
    String order;
    @JsonProperty("name")
    String name;
    @JsonIgnore
    String ignoredField;

}

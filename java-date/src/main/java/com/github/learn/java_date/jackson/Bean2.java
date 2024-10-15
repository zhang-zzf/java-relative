package com.github.learn.java_date.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Bean2 {

    long id;
    @JsonProperty("order")
    String order;
    @JsonProperty("name")
    String name;
    @JsonIgnore
    String ignoredField;

}

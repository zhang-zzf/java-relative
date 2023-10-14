package com.github.learn.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Bean2 {

    @JsonProperty("order")
    private String order;
    @JsonProperty("name")
    private String name;

}

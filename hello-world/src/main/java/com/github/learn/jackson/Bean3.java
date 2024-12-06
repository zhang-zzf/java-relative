package com.github.learn.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonIgnoreProperties({"ignoredField"})
// @JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_NULL)
public class Bean3 {

    Long id;
    @JsonProperty("order")
    String order;
    @JsonProperty("name")
    String name;
    String ignoredField;

}

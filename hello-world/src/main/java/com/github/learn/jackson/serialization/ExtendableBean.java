package com.github.learn.jackson.serialization;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder(alphabetic = true)
public class ExtendableBean {

    String name;
    Map<String, String> properties;

    public ExtendableBean(String name) {
        this.name = name;
    }

    @JsonAnySetter
    public ExtendableBean add(@NotNull String key, String val) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, val);
        return this;
    }

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return properties;
    }

}

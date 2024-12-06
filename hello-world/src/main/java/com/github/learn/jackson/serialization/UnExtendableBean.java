package com.github.learn.jackson.serialization;

import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UnExtendableBean {

    String name;
    Map<String, String> properties;

    public UnExtendableBean(String name) {
        this.name = name;
    }

    public UnExtendableBean add(@NotNull String key, String val) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, val);
        return this;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

}

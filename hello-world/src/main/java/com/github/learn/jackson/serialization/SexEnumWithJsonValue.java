package com.github.learn.jackson.serialization;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum SexEnumWithJsonValue {

    MALE(1, "男"),
    FEMALE(2, "女");

    private final int id;

    private final String name;

    SexEnumWithJsonValue(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

}

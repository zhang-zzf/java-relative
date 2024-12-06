package com.github.learn.jackson.serialization;

import lombok.Getter;

@Getter
public enum SexEnum {

    MALE(1, "male"),
    FEMALE(2, "female");

    private final int id;

    private final String name;

    SexEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

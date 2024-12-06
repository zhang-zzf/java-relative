package com.feng.learn.pattern.singleton;

import lombok.Getter;
import lombok.Setter;

public enum SingletonEnum {
    INSTANCE;

    @Getter
    @Setter
    private long l;
    @Getter
    @Setter
    private String str;

}

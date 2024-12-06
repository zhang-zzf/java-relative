package com.github.learn.cache;

public class DataSource {

    public Integer queryBy(String key) {
        return key.hashCode();
    }

}

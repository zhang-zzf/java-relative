package com.feng.learn.fastjson;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author zhanfeng.zhang
 * @date 2020/08/13
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Person {

    @NonNull
    private Long id;
    private String name;
    private Address address;

    @Data
    public static class Address {
        private String street;
        private String home;
    }
}

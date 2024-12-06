package com.github.learn.mapstruct_demo.object.collection;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhanfeng.zhang
 * @date 2021/11/16
 */
@Data
@Accessors(chain = true)
public class Person {

    private String name;
    private int age;

}

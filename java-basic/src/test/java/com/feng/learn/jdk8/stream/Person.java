package com.feng.learn.jdk8.stream;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @author zhanfeng.zhang
 * @email zhanfeng.zhang@xqchuxing.com
 * @date 2018-10-22 11:02
 **/
@Data
public class Person {

    public enum Sex {
        MALE, FEMALE
    }

    Integer age;
    String name;
    LocalDate birthday;
    Sex gender;
    String emailAddress;

}

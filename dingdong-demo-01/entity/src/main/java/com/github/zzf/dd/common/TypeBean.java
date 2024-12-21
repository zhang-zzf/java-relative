package com.github.zzf.dd.common;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-19
 */
@Data
@JsonTypeInfo(use = Id.CLASS)
public class TypeBean {
    byte byteA = Byte.MIN_VALUE;
    char charA = 'A';
    int i = 1;
    Integer integer = 1;
    long longMax = Long.MAX_VALUE;
    double doubleA = 0.1;
    double doubleMax = Double.MAX_VALUE;
    boolean booleanFalse = false;
    Date createdAt = new Date();
    LocalDateTime updatedAt = LocalDateTime.now();
    TypeBeanSubClass typeBeanSubClass = new TypeBeanSubClass();
    BigDecimal bigDecimalA = new BigDecimal("1.00");

    List<TypeBeanSubClass> typeBeanSubClassList = new ArrayList<TypeBeanSubClass>() {{
        add(new TypeBeanSubClass());
        add(new TypeBeanSubClass());
    }};

    @Data
    public static class TypeBeanSubClass {
        String str = "str";
        List<String> stringList = new ArrayList<String>() {{
            add("str1");
            add("str2");
        }};
    }
}

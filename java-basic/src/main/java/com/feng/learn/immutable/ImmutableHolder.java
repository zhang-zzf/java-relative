package com.feng.learn.immutable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * 声明为final 表示类不能被继承
 */
public final class ImmutableHolder {

    private final int i;
    private final Integer itg;
    private final List<BigDecimal> list;
    private final MutableObject mutableObject;

    // 构造时，保护性拷贝
    public ImmutableHolder(int i, Integer itg, List<BigDecimal> list,
        MutableObject mutableObject) {
        this.i = i; // 不可变，可以直接赋值
        this.itg = itg;// 不可变，可以直接赋值

        // this.list = list; // error 可变对象不可以直接赋值
        this.list = new ArrayList<>(list); // 把List中的所有BigDecimal全部拷贝过来，BigDecimal是不可变对象

        // this.mutableObject = mutableObject; // error 可变对象不可以直接赋值
        this.mutableObject = protectCopyObject(mutableObject);
    }

    public int getI() {
        return i;
    }

    public Integer getItg() {
        return itg;
    }

    // 返回可变对象引用的保护性拷贝
    public List<BigDecimal> getList() {
        // return list; // error 可变对象的引用不可以直接返回
        return new ArrayList<>(list);
    }

    // 返回可变对象引用的保护性拷贝
    public MutableObject getMutableObject() {
        return protectCopyObject(mutableObject);
    }

    private MutableObject protectCopyObject(MutableObject mutableObject) {
        MutableObject ret = new MutableObject();
        ret.setI(mutableObject.getI());
        Map<String, Integer> mutableField = new HashMap<>(mutableObject.getMutableField());
        ret.setMutableField(mutableField);
        return ret;
    }


    @Data
    public static class MutableObject {

        private Integer i;

        private Map<String, Integer> mutableField;

    }
}

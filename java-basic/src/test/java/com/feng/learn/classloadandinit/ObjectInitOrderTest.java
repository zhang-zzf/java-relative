package com.feng.learn.classloadandinit;

import org.junit.Test;

public class ObjectInitOrderTest {

    /**
     * 实例属性在声明时赋值和初始化块中赋值会被提取到构造器的前面
     */
    @Test
    public void testObjectInitOrder() {
        Super s = new Super();
        // ObjectA init..
        // init block 1
        // ObjectB init..
        // init block 2
        // constructor..
    }

    public static class Super {

        ClassInitOrderTest.ObjectA objectA = new ClassInitOrderTest.ObjectA();

        {
            System.out.println("init block 1");
        }

        public Super() {
            System.out.println("constructor..");
        }

        ClassInitOrderTest.ObjectB objectB = new ClassInitOrderTest.ObjectB();

        {
            System.out.println("init block 2");
        }

    }
}

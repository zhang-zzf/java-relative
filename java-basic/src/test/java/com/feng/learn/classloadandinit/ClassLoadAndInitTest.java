package com.feng.learn.classloadandinit;

import org.junit.Test;

/**
 * 类加载/类初始化测试用例 1. 类加载 2. 类初始化
 */
public class ClassLoadAndInitTest {


    /**
     * 以下2种方式只会导致类加载 1. ObjectA.class 2. java.lang.ClassLoader#loadClass(java.lang.String)
     */
    @Test
    public void testClassLoadButNotInitial() throws ClassNotFoundException {
        Class<ObjectA> objectAClass = ObjectA.class;
        // 注意嵌套类的写法
        // com.feng.learn.basic.classloadandinit.ClassLoadAndInitTest$ObjectA
        Class<?> aClass = this.getClass().getClassLoader().loadClass(
            "com.feng.learn.classloadandinit.ClassLoadAndInitTest$ObjectA");

        // 也不会导致类初始化
        Class.forName(
            "com.feng.learn.classloadandinit.ClassLoadAndInitTest$ObjectA",
            false, this.getClass().getClassLoader());
    }


    /**
     * 类被加载且被初始化 1. java.lang.Class#forName(java.lang.String)
     */
    @Test
    public void testLoadAndInitClass() throws ClassNotFoundException {
        Class<?> aClass = Class.forName(
            "com.feng.learn.classloadandinit.ClassLoadAndInitTest$ObjectA");
    }


    public static class ObjectA {

        static {
            System.out.println("Class<ObjectA> init..");
        }

        {
            System.out.println("ObjectA init..");
        }

    }

}

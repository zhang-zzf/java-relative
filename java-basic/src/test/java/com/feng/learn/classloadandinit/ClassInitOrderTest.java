package com.feng.learn.classloadandinit;

import org.junit.Test;

public class ClassInitOrderTest {

    /**
     * 测试类的初始化顺序: 代码书写顺序
     */
    @Test
    public void testClassInitOrder() {
        Super s = new Super();
        // ObjectA init..
        // Super static 1: str2 is null before initialize->null
        // Super static 2: str2 after assignment->init by static block
        // ObjectB init..
    }

    /**
     * 测试接口的初始化: 接口的初始化不会引发父接口的初始化
     */
    @Test
    public void testInterfaceInitOrder() {
        ObjectB objectB = Interface2.objectB; // 仅初始化 Interface2
    }

    /**
     * 测试父子接口继承的初始化: 通过调用 子接口.父接口的属性 只会引发父接口的初始化
     */
    @Test
    public void testInterfaceInitOrder2() {
        ObjectA object = Interface2.objectA; // 仅初始化 Interface1
    }

    /**
     * 和接口测试一样，子类直接引用父类的静态变量，只会导致父类的初始化
     */
    @Test
    public void testClassIndirectReference() {
        ObjectA oa = SubA.oa; // 只初始化SuperA
    }

    /**
     * 测试父子接口继承的初始化 通过调用 子接口.父接口的属性 只会引发父接口的初始化 Interface3.oc 引发 Interface3接口的初始化
     * 在Interface3接口初始化的过程中引用父类(Interface2)的父类(Interface1)的属性，导致Interface1初始化；Interface2不会被初始化
     */
    @Test
    public void testInterfaceInitOrder3() {
        ObjectC o = Interface3.oc;
        // 初始化Interface3
        // 不初始化Interface2
        // 初始化Interface1
    }

    /**
     * 类初始化不会引发实现的父类接口的初始化
     */
    @Test
    public void testClassImplementsInterface() {
        ClassInterface3 c3 = new ClassInterface3();
        // ClassInterface3 实现了 Interface3
        // 但ClassInterface3并没有引用Interface3的静态属性，是不会初始化Interface3
    }


    @Test
    public void testClassIndirectReference2() {
        ObjectB oa = SubA.ob;
        // 初始化SubA
        // 然后诱发初始化SuperA
        // 所以先发初始化SuperA，然后初始化SubA
    }

    /**
     * 数组的间接引用不会引发类的初始化
     */
    @Test
    public void testClassIndirectReference3() {
        SuperA[] array = new SuperA[0];
        // 间接引用，不会触发SuperA的初始化
    }

    // 单例类的初始化
    @Test
    public void testClassInit() {
        SingleTon instance = SingleTon.getInstance();
        System.out.println(SingleTon.count1); // 1
        System.out.println(SingleTon.count2); // 0
    }

    public static class SingleTon {
        private static SingleTon singleTon = new SingleTon();
        public static int count1;
        public static int count2 = 0;

        private SingleTon() {
            count1++;
            count2++;
        }

        public static SingleTon getInstance() {
            return singleTon;
        }
    }


    public static class SuperA {
        public static ObjectA oa = new ObjectA();
    }

    public static class SubA extends SuperA {
        public static ObjectB ob = new ObjectB();
    }

    public interface Interface1 {
        ObjectA objectA = new ObjectA();
    }

    public interface Interface2 extends Interface1 {
        ObjectB objectB = new ObjectB();
    }

    public interface Interface3 extends Interface2 {
        ObjectC oc = new ObjectC();
        ObjectA o = objectA;

        void a();
    }

    public static class ClassInterface3 implements Interface3 {
        static {
            System.out.println("ClassInterface3 init..");
        }

        @Override
        public void a() {
        }
    }


    public static class Super {

        static String str1 = "initialized when declare";
        static String str2; // 加载时初始化成null
        static ObjectA objectA = new ObjectA();

        static {
            System.out.println("Super static 1: str2 is null before initialize->" + str2);
            str2 = "init by static block";
        }

        static {
            System.out.println("Super static 2: str2 after assignment->" + str2);
        }

        static ObjectB objectB = new ObjectB();
    }

    public static class ObjectA {
        {
            System.out.println("ObjectA init..");
        }
    }

    public static class ObjectB {
        {
            System.out.println("ObjectB init..");
        }
    }

    public static class ObjectC {

        {
            System.out.println("ObjectC init..");
        }
    }


}

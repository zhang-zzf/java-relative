package com.feng.learn.classloadandinit;

public class ClassAndObjectInitTest {


    public static void main(String[] args) {
        Singleton instance = Singleton.getInstance(); // 调用类的静态方法->类被加载且初始化
        System.out.println(Singleton.c1);
        System.out.println(Singleton.c2);
    }
}


/**
 * <p>1. 类加载 INSTANCE=null/c1=0/c2=0</p>
 * <p>2. 类初始化
 * <p>2.1 INSTANCE = new Singleton();c1=1;c2=1</p>
 * <p>2.2 c2=0</p>
 */
class Singleton {

    public static final Singleton INSTANCE = new Singleton();
    public static int c1;
    public static int c2 = 0;


    public Singleton() {
        c1 += 1;
        c2 += 1;
    }

    public static Singleton getInstance() {
        return INSTANCE;
    }
}

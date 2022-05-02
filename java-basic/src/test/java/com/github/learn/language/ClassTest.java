package com.github.learn.language;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

interface Interface1 {

    public static Class101 class101 = new Class101();

    public static void staticMethod1() {
        System.out.println("Interface1 staticMethod1");
    }

}

interface Interface2 {

    public static Class102 class102 = new Class102();

}

/**
 * 一个接口可以继承多个接口
 */
interface Interface3 extends Interface1, Interface2 {

    public static Class103 class103 = new Class103();
    public static Class1003 class1003 = new Class1003();

}

/**
 * @author zhanfeng.zhang
 * @date 2022/05/02
 */
class ClassTest {

    @BeforeEach
    public void beforeEachMethod() {
        Holder.holder = "";
    }

    /**
     * static field init
     */

    @Test
    void given_whenClassStaticInit1_then() {
        final String anotherStr = Class1.anotherStr;
        /*
          执行顺序
          声明时初始化会被 cp 到静态初始化块中执行
          static {
              aStr = "Hello, World";
              anotherStr = "你好，世界";
              anotherStr = aStr;
          }
         */
        then(anotherStr).isEqualTo("Hello, World");
    }

    /**
     * <p>只会触发类加载</p>
     * <p>final Class<Class2> class2Class = Class2.class;</p>
     */
    @Test
    void given_whenTriggerClassLoad_then() {
        // 触发类加载,不会会触发类初始化
        final Class<Class2> class2Class = Class2.class;
        then(class2Class).isOfAnyClassIn(Class.class);
    }

    /**
     * static field
     * <p>初始化顺序和 code 书写顺序保持严格一致</p>
     */
    @SneakyThrows
    @Test
    void given_whenTriggerClassInit_then() {
        // 触发类加载和初始化
        /*
            初始化顺序和 code 书写顺序保持严格一致
            static String str1;
            static String str2;
            static Class103 class103;
            static Class104 class104;

            static {
                str1 = "initialized when declare";
                class103 = new Class103();
                System.out.println("Class2 str1: " + str1);
                System.out.println("Class2 str2: str2 is null before initialize->" + str2);
                str2 = "init by static block";
                System.out.println("Class2 str2: str2 after assignment->" + str2);
                class104 = new Class104();
            }

         */
        final Class<?> class2Class = Class.forName("com.github.learn.language.Class2");
        then(class2Class).isOfAnyClassIn(Class.class);
    }

    /**
     * 测试 interface 的初始化
     * <p>Interface3 继承 Interface1 Interface2 ,Interface3.class103 只会触发 Interface3 的初始化</p>
     */
    @Test
    void givenInterface_when_then1() {
        final Class103 class103 = Interface3.class103;
        then(Holder.holder).isEqualTo("Class103Class1003");
    }

    /**
     * 测试 interface 的初始化
     * <p>Interface3 继承 Interface1 Interface2 ,Interface3.class103 只会触发 Interface3 的初始化</p>
     * <p>Interface3 继承 Interface1 Interface2 ,Interface3.class102 只会触发 Interface2 的初始化</p>
     * <p>Interface3 继承 Interface1 Interface2 ,Interface3.class101 只会触发 Interface1 的初始化</p>
     */
    @Test
    void givenInterface_when_then2() {
        final Class102 class103 = Interface3.class102;
        then(Holder.holder).isEqualTo("Class102");
    }

    /**
     * 类不会触发其接口的初始化
     */
    @Test
    void givenInterface_when_then3() {
        final Class107 class107 = Class7.class107;
        then(Holder.holder).isEqualTo("Class107Class7");
    }

    /**
     * 类不会触发其接口的初始化
     * 类访问父接口的 static field ,只会触发单个接口的初始化
     */
    @Test
    void givenInterface_when_then4() {
        final Class103 class103 = Class7.class103;
        then(Holder.holder).isEqualTo("Class103Class1003");
    }

    /**
     * Class1 implements Interface1
     * <p>类不会继承接口的静态方法</p>
     */
    @Test
    void givenClassImplementInterface_when_then() {
        Interface1.staticMethod1();
        // Class1.staticMethod1();
        then(true).isTrue();
    }

}

class Class1 implements Interface1 {

    public static String aStr = "Hello, World";
    public static String anotherStr = aStr;

    static {
        anotherStr = "你好，世界";
    }

}


class Class2 {

    static String str1 = "initialized when declare";
    static String str2; // 加载时初始化成null
    static Class103 class103 = new Class103();
    static Class104 class104 = new Class104();

    static {
        System.out.println("Class2 str1: " + str1);
        System.out.println("Class2 str2: str2 is null before initialize->" + str2);
        str2 = "init by static block";
        System.out.println("Class2 str2: str2 after assignment->" + str2);
    }

}


class Class5 {

    static {
        System.out.println("Class5 init");
        Holder.holder += "Class5";
    }

}

class Class6 extends Class5 {

    public static String str;

    static {
        System.out.println("Class6 init");
        Holder.holder += "Class6";
    }
}


class Class7 implements Interface3 {

    public static Class107 class107 = new Class107();

    static {
        System.out.println("Class7 static init");
        Holder.holder += "Class7";
    }
}

class Holder {

    public static String holder = "";

}

class Class101 {

    static {
        System.out.println("Class101 static init");
        Holder.holder += "Class101";
    }

    {
        System.out.println("Class101 init..");
    }
}

class Class102 {

    static {
        System.out.println("Class102 static init");
        Holder.holder += "Class102";
    }

    {
        System.out.println("Class102 init..");
    }
}


class Class103 {

    static {
        System.out.println("Class103 static init");
        Holder.holder += "Class103";
    }

    {
        System.out.println("Class103 init..");
    }
}

class Class1003 {

    static {
        System.out.println("Class1003 static init");
        Holder.holder += "Class1003";
    }

    {
        System.out.println("Class1003 init..");
    }
}

class Class104 {

    {
        System.out.println("Class104 init..");
    }
}

class Class107 {

    static {
        System.out.println("Class107 static init..");
        Holder.holder += "Class107";
    }

    {
        System.out.println("Class107 init..");
    }
}


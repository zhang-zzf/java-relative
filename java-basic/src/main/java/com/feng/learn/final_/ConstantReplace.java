package com.feng.learn.final_;

public class ConstantReplace {

    // 宏替换
    public static final String NAME = "zhang" + "feng";

    /**
     * 不是宏替换 应为在编译是不能确定STR1的值 static block 中的赋值实在类初始化时进行的。
     */
    public static final String STR1;

    static {
        STR1 = "feng";
    }

    // 宏替换
    // 直接计算成 20.0D
    public final double AGE = 1000 / 50;
    public final String str7 = "178";

    public void constReplace() {
        // true
        System.out.println(str7 + str7 == "178178");
        // 直接编译成
        System.out.println(true);
    }

    /**
     * str5,str6 不是宏替换 编译时不能确定变量的值。
     */
    public final String str5;
    public final String str6;

    {
        str5 = "feng";
    }

    public ConstantReplace() {
        str6 = "zhang";
    }


    public static void main(String[] args) {
        ConstantReplace f = new ConstantReplace();
        // replace
        System.out.println(NAME + NAME == "zhangfengzhangfeng"); // true
        // false
        System.out.println("___: " + f.str7 + f.str7 == "178178");
        // 宏替换
        final String str1 = "178";
        //
        String str2 = "178";
        String str4 = "178";
        //    var10000 = System.out;
        //    var10001 = (new StringBuilder()).append("ppp: ");
        //    Objects.requireNonNull(f);
        //    var10000.println(var10001.append("178" == str2).toString());
        System.out.println("ppp: " + (f.str7 == str2));
        //    System.out.println("000: " + (str2 == str4));
        System.out.println("000: " + (str2 == str4)); // true
        //    System.out.println("111: " + ("178" == str4));
        System.out.println("111: " + (str1 == str4)); // true
        //    System.out.println("222: " + ("178" == str2));
        System.out.println("222: " + (str1 == str2)); // true
        //    System.out.println("333: true");
        System.out.println("333: " + (str1 + str1 == "178178")); // true
        //    System.out.println("444: " + ("178178" == str2 + str2));
        System.out.println("444: " + ("178178" == str2 + str2)); // false

        // 不是宏替换
        final String str3;
        str3 = "hello";
        //    String str3 = "hello";
        //    System.out.println(str3 + str3 == "hellohello");
        System.out.println(str3 + str3 == "hellohello");  // false
    }

}

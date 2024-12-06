package com.feng.learn.final_;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class ConstantReplaceTest {


    @Test
    void givenConst_when_then() {
        ConstantReplace f = new ConstantReplace();
        //         BDDAssertions.then(true).isTrue();
        then(ConstantReplace.NAME + ConstantReplace.NAME == "zhangfengzhangfeng").isTrue(); // true
        // 和预想的不一致
        //        StringBuilder var10000 = new StringBuilder();
        //        Objects.requireNonNull(f);
        //        var10000 = var10000.append("178");
        //        Objects.requireNonNull(f);
        //        BDDAssertions.then(var10000.append("178").toString() == "178178").isFalse();
        then(f.str7 + f.str7 == "178178").isFalse();

        // 宏替换
        final String str1 = "178";
        //
        String str2 = "178";
        String str4 = "178";

        // BDDAssertions.then("178" == "178").isTrue();
        then(f.str7 == str1).isTrue();
        // BDDAssertions.then("178" == str2).isTrue();
        then(f.str7 == str2).isTrue();
        // BDDAssertions.then("178" == str2).isTrue();
        then(str1 == str2).isTrue();
        // BDDAssertions.then("178" == str4).isTrue();
        then(str1 == str4).isTrue();
        // BDDAssertions.then(str2 == str4).isTrue();
        then(str2 == str4).isTrue();
        // BDDAssertions.then(true).isTrue();
        then(str1 + str1 == "178178").isTrue();
        // BDDAssertions.then(str2 + str2 == "178178").isFalse();
        then(str2 + str2 == "178178").isFalse();

        // 不是宏替换
        final String str3;
        str3 = "hello";
        then(str3 + str3 == "hellohello").isFalse();
    }

}
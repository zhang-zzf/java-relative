package com.shuzijun.leetcode.editor.cn;

import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author zhanfeng.zhang
 * @date 2022/04/30
 */
class UtilsTest {


    @Test
    void given2ArrayJsonStr_whenDecode_then() {
        then(Utils.to2Array("[[3,4,5],[3,2,6],[2,2,1]]"))
                .contains(new int[]{3, 2, 6}, Index.atIndex(1));
    }

}
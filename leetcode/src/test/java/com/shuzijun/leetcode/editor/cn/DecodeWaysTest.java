
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class DecodeWaysTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.numDecodings("12")).isEqualTo(2);
        then(solution.numDecodings("226")).isEqualTo(3);
        then(solution.numDecodings("0")).isEqualTo(0);
        // fail case 1
        then(solution.numDecodings("27")).isEqualTo(1);
        // fail case 2
        then(solution.numDecodings("1201234")).isEqualTo(3);
        // fail case 3
        then(solution.numDecodings("2611055971756562")).isEqualTo(4);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int numDecodings(String s) {
            if (s.startsWith("0")) {
                return 0;
            }
            int fn_2 = 1, fn_1 = s.charAt(0) == '0' ? 0 : 1;
            for (int i = 1; i < s.length(); i++) {
                char c, c_1;
                int fn = (((c = s.charAt(i)) != '0') ? fn_1 : 0)
                        // 可以和前一个字符组合解码 "1x" or "20~26"
                        + (((c_1 = s.charAt(i - 1)) == '1' || (c_1 == '2' && c <= '6')) ? fn_2 : 0);
                fn_2 = fn_1;
                fn_1 = fn;
                if (fn_1 == 0) {
                    break;
                }
            }
            return fn_1;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
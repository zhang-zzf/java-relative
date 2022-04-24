
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class IsSubsequenceTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.isSubsequence("abc", "ahdbgdc")).isTrue();
        then(solution.isSubsequence("axc", "ahdbgdc")).isFalse();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public boolean isSubsequence(String s, String t) {
            if (s.length() == 0) {
                return true;
            }
            int srcIdx = 0;
            char c = s.charAt(srcIdx);
            for (int i = 0; i < t.length(); i++) {
                if (t.charAt(i) == c) {
                    if (++srcIdx < s.length()) {
                        c = s.charAt(srcIdx);
                    } else {
                        break;
                    }
                }
            }
            return srcIdx == s.length();
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
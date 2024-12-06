package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class ShortestWayToFormStringTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.shortestWay("xyz", "xzyxz")).isEqualTo(3);
        then(solution.shortestWay("aaaa", "aaaaaaaaaaaaa")).isEqualTo(4);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int shortestWay(String source, String target) {
            int ans = 1;
            int srcIdx = 0;
            for (int i = 0; i < target.length(); i++) {
                int t = source.indexOf(target.charAt(i), srcIdx);
                if (t == -1) {
                    t = source.indexOf(target.charAt(i));
                    if (t == -1) {
                        ans = -1;
                        break;
                    }
                    ans += 1;
                }
                srcIdx = t + 1;
            }
            return ans;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
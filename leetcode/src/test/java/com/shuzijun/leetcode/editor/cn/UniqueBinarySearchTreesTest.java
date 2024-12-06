package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class UniqueBinarySearchTreesTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.numTrees(3)).isEqualTo(5);
        then(solution.numTrees(4)).isEqualTo(14);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int numTrees(int n) {
            int[] dp = new int[n + 1];
            dp[0] = 1;
            dp[1] = 1;
            for (int i = 2; i <= n; i++) {
                int fn = 0;
                for (int j = 1; j <= i; j++) {
                    fn += dp[j - 1] * dp[i - j];
                }
                dp[i] = fn;
            }
            return dp[n];
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}

package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.assertj.core.api.BDDAssertions.then;


class MaximumLengthOfPairChainTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int ans = solution.findLongestChain(new int[][]{
                new int[]{1, 2},
                new int[]{2, 3},
                new int[]{3, 4},
        });
        then(ans).isEqualTo(2);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int findLongestChain(int[][] pairs) {
            int ans = 0;
            // 以第一个数组排序
            Arrays.sort(pairs, Comparator.comparing(arr -> arr[0]));
            int[] dp = new int[pairs.length];
            for (int i = 0; i < pairs.length; i++) {
                dp[i] = 1;
                for (int j = 0; j < i; j++) {
                    if (pairs[j][1] < pairs[i][0]) {
                        dp[i] = Math.max(dp[i], dp[j] + 1);
                    }
                }
                ans = Math.max(ans, dp[i]);
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
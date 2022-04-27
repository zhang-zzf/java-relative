
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

        // 具象问题：每个 pair 代表一个工作，pair 表示工作的开始时间和结束时间，选择最多的工作数量
        // 选择不选择的问题
        public int findLongestChain(int[][] pairs) {
            // 按第一个数字排序
            Arrays.sort(pairs, Comparator.comparing(arr -> arr[0]));
            final int lng = pairs.length;
            int[] dp = new int[lng + 1];
            dp[0] = 1;
            for (int i = 0; i < lng; i++) {
                int maxPair = dp[i];
                for (int j = i - 1; j >= 0; j--) {
                    if (pairs[j][1] < pairs[i][0]) {
                        maxPair = Math.max(maxPair, dp[j + 1] + 1);
                        break;
                    }
                }
                dp[i + 1] = maxPair;
            }
            return dp[lng];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}

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

        // 贪心
        public int findLongestChain(int[][] pairs) {
            Arrays.sort(pairs, Comparator.comparing(arr -> arr[1]));
            int curEnd = Integer.MIN_VALUE;
            int ans = 0;
            for (int[] pair : pairs) {
                if (pair[0] > curEnd) {
                    ans += 1;
                    curEnd = pair[1];
                }
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
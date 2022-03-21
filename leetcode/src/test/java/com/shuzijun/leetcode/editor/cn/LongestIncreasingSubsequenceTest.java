
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class LongestIncreasingSubsequenceTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int lengthOfLIS = solution.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18});
        then(lengthOfLIS).isEqualTo(4);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int lengthOfLIS(int[] nums) {
            int[] tails = new int[nums.length];
            // tails 中的元素个数
            int res = 0;
            for (int num : nums) {
                int i = 0, j = res;
                while (i < j) {
                    int m = i + (j - i) / 2;
                    if (tails[m] < num) {
                        i = m + 1;
                    } else {
                        j = m;
                    }
                }
                tails[i] = num;
                res = i == res ? res + 1 : res;
            }
            return res;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
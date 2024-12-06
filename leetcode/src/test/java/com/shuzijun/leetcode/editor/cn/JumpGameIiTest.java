package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class JumpGameIiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.jump(new int[]{2, 3, 1, 1, 4, 2, 6})).isEqualTo(3);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int jump(int[] nums) {
            int maxPosition = 0, end = maxPosition;
            int step = 0;
            // 核心点：不必要访问最后一个元素。
            for (int i = 0; i < nums.length - 1; i++) {
                maxPosition = Math.max(maxPosition, i + nums[i]);
                if (i == end) {
                    end = maxPosition;
                    step += 1;
                }
            }
            return step;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
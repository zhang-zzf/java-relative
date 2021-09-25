//给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。 
//
// 
//
// 示例 1： 
//
// 
//
// 
//输入：height = [0,1,0,2,1,0,1,3,2,1,2,1]
//输出：6
//解释：上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。 
// 
//
// 示例 2： 
//
// 
//输入：height = [4,2,0,3,2,5]
//输出：9
// 
//
// 
//
// 提示： 
//
// 
// n == height.length 
// 0 <= n <= 3 * 10⁴ 
// 0 <= height[i] <= 10⁵ 
// 
// Related Topics 栈 数组 双指针 动态规划 单调栈 👍 2715 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class TrappingRainWaterTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1})).isEqualTo(6);
        then(solution.trap(new int[]{4, 2, 0, 3, 2, 5})).isEqualTo(9);
        then(solution.trap(new int[]{5, 4, 1, 2})).isEqualTo(1);
        then(solution.trap(new int[]{9, 6, 8, 8, 5, 6, 3})).isEqualTo(3);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int trap(int[] height) {
            if (height.length < 3) {
                return 0;
            }
            int dp[] = new int[height.length];
            int left = 0, right = 1;
            for (; right < height.length; right++) {
                if (height[right] <= height[right - 1]) {
                    continue;
                }
                int slotHeight = height[left] == 0 ? 0 : Math.min(height[left], height[right]);
                // 站在全局的角度看： 中间所有的雨水量清 0；
                int heightSum = 0;
                for (int i = right - 1; i > left; i--) {
                    int sub = slotHeight - height[i];
                    if (sub <= 0) {
                        break;
                    }
                    heightSum += sub;
                    dp[i] = 0;
                }
                dp[right] = heightSum;
                // 右指针 >= 左指针，右指针向右移动时，和左指针之间不会容纳更多的雨水，迁移左指针到右指针位置
                if (height[right] >= height[left]) {
                    left = right;
                }
            }
            int sum = 0;
            for (int i : dp) {
                sum += i;
            }
            return sum;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
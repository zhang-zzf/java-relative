// 给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i,
// ai) 和 (i, 0) 。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
//
// 说明：你不能倾斜容器。 
//
// 
//
// 示例 1： 
//
// 
//
// 
// 输入：[1,8,6,2,5,4,8,3,7]
// 输出：49
// 解释：图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为 49。
//
// 示例 2： 
//
// 
// 输入：height = [1,1]
// 输出：1
// 
//
// 示例 3： 
//
// 
// 输入：height = [4,3,2,1,4]
// 输出：16
// 
//
// 示例 4： 
//
// 
// 输入：height = [1,2,1]
// 输出：2
// 
//
// 
//
// 提示： 
//
// 
// n = height.length 
// 2 <= n <= 3 * 10⁴ 
// 0 <= height[i] <= 3 * 10⁴ 
// 
// Related Topics 贪心 数组 双指针 👍 2720 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class ContainerWithMostWaterTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        int ret = solution.maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7});
        then(ret).isEqualTo(49);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxArea(int[] height) {
            int left = 0, right = height.length - 1;
            int ret = (right - left) * (Math.min(height[left], height[right]));
            while (left < right) {
                if (height[left] < height[right]) {
                    left += 1;
                }
                else {
                    right -= 1;
                }
                int tmp = (right - left) * Math.min(height[left], height[right]);
                ret = tmp > ret ? tmp : ret;
            }
            return ret;
        }
    }
    // leetcode submit region end(Prohibit modification and deletion)


}
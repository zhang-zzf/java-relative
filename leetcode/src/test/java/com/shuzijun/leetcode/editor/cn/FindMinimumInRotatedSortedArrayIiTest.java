// 已知一个长度为 n 的数组，预先按照升序排列，经由 1 到 n 次 旋转 后，得到输入数组。例如，原数组 nums = [0,1,4,4,5,6,7] 在变
// 化后可能得到：
// 
// 若旋转 4 次，则可以得到 [4,5,6,7,0,1,4] 
// 若旋转 7 次，则可以得到 [0,1,4,4,5,6,7] 
// 
//
// 注意，数组 [a[0], a[1], a[2], ..., a[n-1]] 旋转一次 的结果为数组 [a[n-1], a[0], a[1], a[2], 
//..., a[n-2]] 。 
//
// 给你一个可能存在 重复 元素值的数组 nums ，它原来是一个升序排列的数组，并按上述情形进行了多次旋转。请你找出并返回数组中的 最小元素 。 
//
// 
//
// 示例 1： 
//
// 
// 输入：nums = [1,3,5]
// 输出：1
// 
//
// 示例 2： 
//
// 
// 输入：nums = [2,2,2,0,1]
// 输出：0
// 
//
// 
//
// 提示： 
//
// 
// n == nums.length 
// 1 <= n <= 5000 
// -5000 <= nums[i] <= 5000 
// nums 原来是一个升序排序的数组，并进行了 1 至 n 次旋转 
// 
//
// 
//
// 进阶： 
//
// 
// 这道题是 寻找旋转排序数组中的最小值 的延伸题目。 
// 允许重复会影响算法的时间复杂度吗？会如何影响，为什么？ 
// 
// Related Topics 数组 二分查找 👍 453 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class FindMinimumInRotatedSortedArrayIiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.findMin(new int[]{2, 2, 2, 0, 1})).isEqualTo(0);
        then(solution.findMin(new int[]{1, 2, 2, 2, 0})).isEqualTo(0);
        then(solution.findMin(new int[]{0, 1, 2, 2, 2})).isEqualTo(0);
        then(solution.findMin(new int[]{2, 2, 2})).isEqualTo(2);
        // fail case: 3313
        then(solution.findMin(new int[]{3, 3, 1, 3})).isEqualTo(1);
        // fail case: 10,1,10,10,10
        then(solution.findMin(new int[]{10, 1, 10, 10, 10})).isEqualTo(1);
        // fail case: 1,1,0,1,1,1,1,1,1,1,1,1
        then(solution.findMin(new int[]{1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1})).isEqualTo(0);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int findMin(int[] nums) {
            int left = 0, right = nums.length - 1;
            while (left < right) {
                int mid = left + ((right - left) >> 1);
                if (nums[mid] < nums[right]) {
                    right = mid;
                }
                else if (nums[mid] > nums[right]) {
                    left = mid + 1;
                }
                else {
                    // nums[mid] == nums[right]
                    // 无法判断最小值是在左侧还是右侧。
                    // 有2种情况可以确定
                    // 1. 最小值不是 nums[mid]
                    // 2. 最小值是 nums[mid]
                    // 以上2种情况下把 right 排除在查找范围外不影响最终最小值
                    right -= 1;
                }
            }
            return nums[left];
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
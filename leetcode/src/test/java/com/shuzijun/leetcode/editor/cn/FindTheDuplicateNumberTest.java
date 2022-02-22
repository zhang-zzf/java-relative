//给定一个包含 n + 1 个整数的数组 nums ，其数字都在 [1, n] 范围内（包括 1 和 n），可知至少存在一个重复的整数。 
//
// 假设 nums 只有 一个重复的整数 ，返回 这个重复的数 。 
//
// 你设计的解决方案必须 不修改 数组 nums 且只用常量级 O(1) 的额外空间。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [1,3,4,2,2]
//输出：2
// 
//
// 示例 2： 
//
// 
//输入：nums = [3,1,3,4,2]
//输出：3
// 
//
// 
//
// 提示： 
//
// 
// 1 <= n <= 10⁵ 
// nums.length == n + 1 
// 1 <= nums[i] <= n 
// nums 中 只有一个整数 出现 两次或多次 ，其余整数均只出现 一次 
// 
//
// 
//
// 进阶： 
//
// 
// 如何证明 nums 中至少存在一个重复的数字? 
// 你可以设计一个线性级时间复杂度 O(n) 的解决方案吗？ 
// 
// Related Topics 位运算 数组 双指针 二分查找 👍 1583 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class FindTheDuplicateNumberTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.findDuplicate(new int[]{1, 3, 4, 2, 2})).isEqualTo(2);
        then(solution.findDuplicate(new int[]{1, 4, 4, 2, 4})).isEqualTo(4);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int findDuplicate(int[] nums) {
            // 值域 [1,n] n = nums.length - 1
            int left = 1, right = nums.length - 1;
            while (left < right) {
                int mid = left + ((right - left) >> 1);
                int cnt = getCntLEThanMid(nums, mid);
                if (cnt > mid) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            return left;
        }

        private int getCntLEThanMid(int[] nums, int pivot) {
            int ans = 0;
            for (int num : nums) {
                ans += ((num <= pivot) ? 1 : 0);
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
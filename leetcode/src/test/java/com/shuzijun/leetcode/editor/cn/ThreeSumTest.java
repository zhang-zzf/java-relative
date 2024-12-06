// Code Template
// 给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j !=
// k ，同时还满足 nums[i] + nums[j] + nums[k] == 0 。请
//
// 你返回所有和为 0 且不重复的三元组。 
//
// 注意：答案中不可以包含重复的三元组。 
//
// 
//
// 
//
// 示例 1： 
//
// 
// 输入：nums = [-1,0,1,2,-1,-4]
// 输出：[[-1,-1,2],[-1,0,1]]
// 解释：
// nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0 。
// nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0 。
// nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0 。
// 不同的三元组是 [-1,0,1] 和 [-1,-1,2] 。
// 注意，输出的顺序和三元组的顺序并不重要。
// 
//
// 示例 2： 
//
// 
// 输入：nums = [0,1,1]
// 输出：[]
// 解释：唯一可能的三元组和不为 0 。
// 
//
// 示例 3： 
//
// 
// 输入：nums = [0,0,0]
// 输出：[[0,0,0]]
// 解释：唯一可能的三元组和为 0 。
// 
//
// 
//
// 提示： 
//
// 
// 3 <= nums.length <= 3000 
// -10⁵ <= nums[i] <= 10⁵ 
// 
//
// Related Topics 数组 双指针 排序 
// 👍 5425 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;


public class ThreeSumTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        List<List<Integer>> ans = solution.threeSum(new int[]{-1, 0, 1, 2, -1, -4});
        then(ans).hasSize(2);
    }

    @Test
    void givenCase1_when_then() {
        List<List<Integer>> lists = solution.threeSum(new int[]{0, 0, 0, 0});
        then(lists).hasSize(1);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public List<List<Integer>> threeSum(int[] nums) {
            List<List<Integer>> ans = new ArrayList<>();
            int target = 0;
            // 不修改入参
            nums = Arrays.copyOf(nums, nums.length);
            // 原址排序
            Arrays.sort(nums);
            // 去重复
            // int idx = 0;
            // for (int i = 0; i < nums.length; i++) {
            //     if (nums[i] != nums[idx]) {
            //         nums[++idx] = nums[i];
            //     }
            // }
            // nums = Arrays.copyOf(nums, idx + 1);
            for (int i = 0; i < nums.length - 2; i++) {
                int n1 = nums[i];
                if (n1 > target) {
                    break;
                }
                // n1 去重复
                if (i > 0 && nums[i] == nums[i - 1]) {
                    continue;
                }
                int left = i + 1, right = nums.length - 1;
                while (left < right) {
                    int n2 = nums[left], n3 = nums[right];
                    int sum = n1 + n2 + n3;
                    if (sum == target) {
                        ans.add(Arrays.asList(n1, n2, n3));
                        // 向中间靠拢
                        while (left < right && nums[left] == nums[left + 1]) {
                            left += 1;
                        }
                        while (left < right && nums[right] == nums[right - 1]) {
                            right -= 1;
                        }
                        left += 1;
                        right -= 1;
                    }
                    else if (sum < target) {
                        // 此步不需要去重复
                        left += 1;
                    }
                    else {
                        // sum > target
                        // 此步不需要去重复
                        right -= 1;
                    }
                }
            }
            return ans;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
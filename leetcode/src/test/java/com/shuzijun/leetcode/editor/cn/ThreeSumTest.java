//给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有和为 0 且不重
//复的三元组。 
//
// 注意：答案中不可以包含重复的三元组。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [-1,0,1,2,-1,-4]
//输出：[[-1,-1,2],[-1,0,1]]
// 
//
// 示例 2： 
//
// 
//输入：nums = []
//输出：[]
// 
//
// 示例 3： 
//
// 
//输入：nums = [0]
//输出：[]
// 
//
// 
//
// 提示： 
//
// 
// 0 <= nums.length <= 3000 
// -10⁵ <= nums[i] <= 10⁵ 
// 
// Related Topics 数组 双指针 排序 👍 3738 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;


public class ThreeSumTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        int[] nums = new int[]{-1, 0, 1, 2, -1, -4};
        final List<List<Integer>> lists = solution.threeSum(nums);
        then(lists).isNotNull();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public List<List<Integer>> threeSum(int[] nums) {
            final List<List<Integer>> ret = new ArrayList<>(8);
            if (nums.length < 3) {
                return ret;
            }
            final int[] copyOf = Arrays.copyOf(nums, nums.length);
            Arrays.sort(copyOf);
            for (int i = 0; i < copyOf.length; i++) {
                if (copyOf[i] > 0) {
                    break;
                }
                if (i > 0 && copyOf[i] == copyOf[i - 1]) {
                    continue;
                }
                int r = copyOf.length - 1;
                for (int l = i + 1; l < r; l++) {
                    if (l > i + 1 && copyOf[l] == copyOf[l - 1]) {
                        continue;
                    }
                    while (l < r && copyOf[r] + copyOf[l] + copyOf[i] > 0) {
                        r -= 1;
                    }
                    if (l >= r) {
                        break;
                    }
                    if (copyOf[i] + copyOf[l] + copyOf[r] == 0) {
                        ret.add(Arrays.asList(copyOf[i], copyOf[l], copyOf[r]));
                    }
                }
            }
            return ret;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
//给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [1,2,3]
//输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
// 
//
// 示例 2： 
//
// 
//输入：nums = [0,1]
//输出：[[0,1],[1,0]]
// 
//
// 示例 3： 
//
// 
//输入：nums = [1]
//输出：[[1]]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 6 
// -10 <= nums[i] <= 10 
// nums 中的所有整数 互不相同 
// 
// Related Topics 数组 回溯 👍 1700 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;


public class PermutationsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final List<List<Integer>> lists = solution.permute(new int[]{1, 2, 3});
        then(lists).hasSize(6);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public List<List<Integer>> permute(int[] nums) {
            List<List<Integer>> ret = new ArrayList<>();
            List<Integer> list = new ArrayList<>(3);
            backTrack(nums, ret, list);
            return ret;
        }

        private void backTrack(int[] nums, List<List<Integer>> ret, List<Integer> list) {
            // 遍历结束条件
            if (list.size() == nums.length) {
                ret.add(new ArrayList<>(list));
                return;
            }
            for (int i = 0; i < nums.length; i++) {
                // 选择
                if (list.contains(nums[i])) {
                    continue;
                }
                list.add(nums[i]);
                backTrack(nums, ret, list);
                // 撤销最后一次选择
                list.remove(list.size() - 1);
            }
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
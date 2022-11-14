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

        /**
         * 解答思路：https://www.yuque.com/u1147067/vzaha9/zdbwvt#xb3V1
         */
        public List<List<Integer>> permute(int[] nums) {
            List<List<Integer>> ret = new ArrayList<>();
            // 保存已经选择的数字的下标
            backTrack(nums, new ArrayList<>(nums.length), ret);
            return ret;
        }

        private void backTrack(int[] nums, List<Integer> track, List<List<Integer>> ret) {
            if (track.size() == nums.length) {
                // 保存一次全排列
                List aResult = new ArrayList<Integer>(nums.length);
                for (int i = 0; i < nums.length; i++) {
                    aResult.add(nums[track.get(i)]);
                }
                ret.add(aResult);
            }
            for (int i = 0; i < nums.length; i++) {
                if (track.contains(i)) {
                    continue;
                }
                // 选择
                track.add(i);
                backTrack(nums, track, ret);
                // 撤销选择
                track.remove(track.size() - 1);
            }
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
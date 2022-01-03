//给定一个可包含重复数字的序列 nums ，按任意顺序 返回所有不重复的全排列。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [1,1,2]
//输出：
//[[1,1,2],
// [1,2,1],
// [2,1,1]]
// 
//
// 示例 2： 
//
// 
//输入：nums = [1,2,3]
//输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 8 
// -10 <= nums[i] <= 10 
// 
// Related Topics 数组 回溯 👍 894 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.BDDAssertions.then;


public class PermutationsIiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final List<List<Integer>> lists = solution.permuteUnique(new int[]{1, 1, 2, 2});
        then(lists).hasSize(6);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        /**
         * 解答思路：https://www.yuque.com/u1147067/vzaha9/zdbwvt#xb3V1
         */
        public List<List<Integer>> permuteUnique(int[] nums) {
            final List<List<Integer>> ret = new ArrayList<>();
            // 代表当前链路上已选择的数字的下标
            final List<Integer> list = new ArrayList<>();
            backTrack(nums, ret, list);
            return ret;
        }

        private void backTrack(int[] nums, List<List<Integer>> ret, List<Integer> list) {
            if (list.size() == nums.length) {
                // 保存一次全排列
                List aResult = new ArrayList<Integer>(nums.length);
                for (int i = 0; i < nums.length; i++) {
                    aResult.add(nums[list.get(i)]);
                }
                ret.add(aResult);
            }
            Set<Integer> curSelectedNum = new HashSet<>(nums.length);
            for (int i = 0; i < nums.length; i++) {
                if (list.contains(i)) {
                    continue;
                }
                // 剪枝
                if (curSelectedNum.contains(nums[i])) {
                    continue;
                }
                curSelectedNum.add(nums[i]);
                // 选择
                list.add(i);
                backTrack(nums, ret, list);
                // 撤销选择
                list.remove(list.size() - 1);
            }
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
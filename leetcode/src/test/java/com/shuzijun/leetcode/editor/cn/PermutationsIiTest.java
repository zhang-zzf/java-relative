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

        public List<List<Integer>> permuteUnique(int[] nums) {
            final List<List<Integer>> ret = new ArrayList<>();
            final List<Integer> list = new ArrayList<>();
            // 代表当前链路上已选择的数字的下标
            Set<Integer> used = new HashSet<>(nums.length);
            backTrack(nums, ret, list, used);
            return ret;
        }

        private void backTrack(int[] nums, List<List<Integer>> ret, List<Integer> list, Set<Integer> used) {
            // 到达叶子节点
            if (list.size() == nums.length) {
                ret.add(new ArrayList<>(list));
                return;
            }
            Set<Integer> curSelectedNum = new HashSet<>(nums.length);
            for (int i = 0; i < nums.length; i++) {
                if (used.contains(i)) {
                    continue;
                }
                if (curSelectedNum.contains(nums[i])) {
                    continue;
                }
                // 选择
                curSelectedNum.add(nums[i]);
                used.add(i);
                list.add(nums[i]);
                backTrack(nums, ret, list, used);
                list.remove(list.size() - 1);
                used.remove(i);
            }
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
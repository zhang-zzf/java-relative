// 给你一个整数数组 nums ，数组中的元素 互不相同 。返回该数组所有可能的子集（幂集）。
//
// 解集 不能 包含重复的子集。你可以按 任意顺序 返回解集。 
//
// 
//
// 示例 1： 
//
// 
// 输入：nums = [1,2,3]
// 输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
// 
//
// 示例 2： 
//
// 
// 输入：nums = [0]
// 输出：[[],[0]]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 10 
// -10 <= nums[i] <= 10 
// nums 中的所有元素 互不相同 
// 
// Related Topics 位运算 数组 回溯 👍 1443 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


public class SubsetsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final List<List<Integer>> lists = solution.subsets(new int[]{1, 2, 3});
        then(lists).hasSize(8);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public List<List<Integer>> subsets(int[] nums) {
            List<List<Integer>> ret = new ArrayList<>();
            for (int i = 0; i <= nums.length; i++) {
                backTrack(nums, i, 0, new ArrayList<>(), ret);
            }
            return ret;
        }

        private void backTrack(int[] nums, int k, int idx, List<Integer> track,
            List<List<Integer>> ret) {
            if (track.size() == k) {
                ret.add(new ArrayList<>(track));
                return;
            }
            for (int i = idx; i < nums.length; i++) {
                // 选择
                track.add(nums[i]);
                backTrack(nums, k, i + 1, track, ret);
                track.remove(track.size() - 1);
            }
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
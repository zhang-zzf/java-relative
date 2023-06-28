//给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）。 
//
// 解集 不能 包含重复的子集。返回的解集中，子集可以按 任意顺序 排列。 
//
// 
// 
// 
//
// 示例 1： 
//
// 
//输入：nums = [1,2,2]
//输出：[[],[1],[1,2],[1,2,2],[2],[2,2]]
// 
//
// 示例 2： 
//
// 
//输入：nums = [0]
//输出：[[],[0]]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 10 
// -10 <= nums[i] <= 10 
// 
// 
// 
// Related Topics 位运算 数组 回溯 👍 718 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;


public class SubsetsIiTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final List<List<Integer>> lists = solution.subsetsWithDup(new int[]{1, 2, 2});
    then(lists).hasSize(6);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public List<List<Integer>> subsetsWithDup(int[] nums) {
      // 有重复的组合，先排序
      final int[] copy = Arrays.copyOf(nums, nums.length);
      Arrays.sort(copy);
      List<List<Integer>> ret = new ArrayList<>();
      for (int i = 0; i <= nums.length; i++) {
        backTrack(copy, i, 0, new ArrayList<>(), ret);
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
        // nums 经过排序，使用 nums[i]=nums[i-1] 快速判断去重复
        if (i > idx && nums[i] == nums[i - 1]) {
          // 剪枝
          continue;
        }
        // 选择
        track.add(nums[i]);
        backTrack(nums, k, i + 1, track, ret);
        track.remove(track.size() - 1);
      }
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
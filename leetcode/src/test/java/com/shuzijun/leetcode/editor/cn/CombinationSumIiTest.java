//给定一个数组 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。 
//
// candidates 中的每个数字在每个组合中只能使用一次。 
//
// 注意：解集不能包含重复的组合。 
//
// 
//
// 示例 1: 
//
// 
//输入: candidates = [10,1,2,7,6,1,5], target = 8,
//输出:
//[
//[1,1,6],
//[1,2,5],
//[1,7],
//[2,6]
//] 
//
// 示例 2: 
//
// 
//输入: candidates = [2,5,2,1,2], target = 5,
//输出:
//[
//[1,2,2],
//[5]
//] 
//
// 
//
// 提示: 
//
// 
// 1 <= candidates.length <= 100 
// 1 <= candidates[i] <= 50 
// 1 <= target <= 30 
// 
// Related Topics 数组 回溯 👍 788 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;


public class CombinationSumIiTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final List<List<Integer>> lists = solution.combinationSum2(new int[]{10, 1, 2, 7, 6, 1, 5}, 8);
    then(lists).hasSize(4);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
      List<List<Integer>> ret = new ArrayList<>();
      final int[] ints = Arrays.copyOf(candidates, candidates.length);
      Arrays.sort(ints);
      List<Integer> track = new ArrayList<>();
      backTrack(ints, target, 0, track, ret);
      return ret;
    }

    private void backTrack(int[] candidates, int target, int idx, List<Integer> track,
        List<List<Integer>> ret) {
      if (target == 0) {
        ret.add(new ArrayList<>(track));
        return;
      }
      if (target < 0) {
        return;
      }
      Set<Integer> selected = new HashSet<>(candidates.length);
      for (int i = idx; i < candidates.length; i++) {
        final int candidate = candidates[i];
        // 剪枝
        if (!selected.add(candidate)) {
          continue;
        }
        // 选择
        track.add(candidate);
        // idx = i + 1 向后迁移一位
        backTrack(candidates, target - candidate, i + 1, track, ret);
        track.remove(track.size() - 1);
      }
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
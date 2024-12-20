// 给你一个 无重复元素 的整数数组 candidates 和一个目标整数 target ，找出 candidates 中可以使数字和为目标数 target 的
// 所有不同组合 ，并以列表形式返回。你可以按 任意顺序 返回这些组合。 
//
// candidates 中的 同一个 数字可以 无限制重复被选取 。如果至少一个数字的被选数量不同，则两种组合是不同的。 
//
// 对于给定的输入，保证和为 target 的不同组合数少于 150 个。 
//
// 
//
// 示例 1： 
//
// 
// 输入：candidates = [2,3,6,7], target = 7
// 输出：[[2,2,3],[7]]
// 解释：
// 2 和 3 可以形成一组候选，2 + 2 + 3 = 7 。注意 2 可以使用多次。
// 7 也是一个候选， 7 = 7 。
// 仅有这两种组合。
//
// 示例 2： 
//
// 
// 输入: candidates = [2,3,5], target = 8
// 输出: [[2,2,2,2],[2,3,3],[3,5]]
//
// 示例 3： 
//
// 
// 输入: candidates = [2], target = 1
// 输出: []
// 
//
// 示例 4： 
//
// 
// 输入: candidates = [1], target = 1
// 输出: [[1]]
// 
//
// 示例 5： 
//
// 
// 输入: candidates = [1], target = 2
// 输出: [[1,1]]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= candidates.length <= 30 
// 1 <= candidates[i] <= 200 
// candidate 中的每个元素都 互不相同 
// 1 <= target <= 500 
// 
// Related Topics 数组 回溯 👍 1690 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


public class CombinationSumTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final List<List<Integer>> lists = solution.combinationSum(new int[]{2, 3, 6, 7}, 7);
        then(lists).hasSize(2);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        /**
         * 解题思路 https://www.yuque.com/u1147067/vzaha9/wlvhdn#bIUZ9
         */
        public List<List<Integer>> combinationSum(int[] candidates, int target) {
            List<List<Integer>> ret = new ArrayList<>();
            backTrack(candidates, target, 0, new ArrayList<>(), ret);
            return ret;
        }

        private void backTrack(int[] candidates, int target, int idx, List<Integer> track,
            List<List<Integer>> ret) {
            if (target == 0) {
                // 转结果
                ret.add(new ArrayList<>(track));
                return;
            }
            if (target < 0) {
                return;
            }
            for (int i = idx; i < candidates.length; i++) {
                track.add(candidates[i]);
                backTrack(candidates, target - candidates[i], i, track, ret);
                track.remove(track.size() - 1);
            }
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
// 给定两个整数 n 和 k，返回范围 [1, n] 中所有可能的 k 个数的组合。
//
// 你可以按 任何顺序 返回答案。 
//
// 
//
// 示例 1： 
//
// 
// 输入：n = 4, k = 2
// 输出：
//[
//  [2,4],
//  [3,4],
//  [2,3],
//  [1,2],
//  [1,3],
//  [1,4],
//] 
//
// 示例 2： 
//
// 
// 输入：n = 1, k = 1
// 输出：[[1]]
//
// 
//
// 提示： 
//
// 
// 1 <= n <= 20 
// 1 <= k <= n 
// 
// Related Topics 数组 回溯 👍 819 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


public class CombinationsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final List<List<Integer>> lists = solution.combine(4, 2);
        then(lists).hasSize(6);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public List<List<Integer>> combine(int n, int k) {
            List<List<Integer>> ret = new ArrayList<>();
            List<Integer> track = new ArrayList<>();
            backTrack(n, k, 1, track, ret);
            return ret;
        }

        private void backTrack(int n, int k, int idx, List<Integer> track, List<List<Integer>> ret) {
            if (track.size() == k) {
                ret.add(new ArrayList<>(track));
                return;
            }
            for (int i = idx; i <= n; i++) {
                track.add(i);
                backTrack(n, k, i + 1, track, ret);
                track.remove(track.size() - 1);
            }
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
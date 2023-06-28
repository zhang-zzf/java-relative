//给出集合 [1,2,3,...,n]，其所有元素共有 n! 种排列。 
//
// 按大小顺序列出所有排列情况，并一一标记，当 n = 3 时, 所有排列如下： 
//
// 
// "123" 
// "132" 
// "213" 
// "231" 
// "312" 
// "321" 
// 
//
// 给定 n 和 k，返回第 k 个排列。 
//
// 
//
// 示例 1： 
//
// 
//输入：n = 3, k = 3
//输出："213"
// 
//
// 示例 2： 
//
// 
//输入：n = 4, k = 9
//输出："2314"
// 
//
// 示例 3： 
//
// 
//输入：n = 3, k = 1
//输出："123"
// 
//
// 
//
// 提示： 
//
// 
// 1 <= n <= 9 
// 1 <= k <= n! 
// 
// Related Topics 递归 数学 👍 611 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;


public class PermutationSequenceTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    then(solution.getPermutation(4, 9)).isEqualTo("2314");
    then(solution.getPermutation(3, 2)).isEqualTo("132");
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public String getPermutation(int n, int k) {
      // 已选择的下标
      List<Integer> track = new ArrayList<>();
      int depth = 1;
      int curNo = 0;
      while (track.size() < n) {
        int items = factorial(n - depth);
        for (int i = 1; i <= n; i++) {
          if (track.contains(i)) {
            continue;
          }
          if (curNo + items < k) {
            curNo += items;
            continue;
          }
          track.add(i);
          break;
        }
        depth += 1;
      }
      StringBuilder buf = new StringBuilder(n);
      for (Integer integer : track) {
        buf.append(integer);
      }
      return buf.toString();
    }

    private int factorial(int n) {
      int ret = 1;
      for (int i = 1; i <= n; i++) {
        ret *= i;
      }
      return ret;
    }

    private void backTrack(int n, int k, AtomicInteger no, List<Integer> track, List<Integer> ret) {
      if (track.size() == n) {
        no.addAndGet(1);
        if (no.get() == k) {
          ret.addAll(track);
        }
        return;
      }
      if (no.get() >= k) {
        return;
      }
      for (int i = 1; i <= n; i++) {
        // 选择
        if (track.contains(i)) {
          continue;
        }
        // 当前子树的分支最大不超过 k， 直接跳过
        final int maxK = no.get() + factorial(n - track.size() - 1);
        if (maxK < k) {
          no.set(maxK);
          continue;
        }
        track.add(i);
        backTrack(n, k, no, track, ret);
        track.remove(track.size() - 1);
      }
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
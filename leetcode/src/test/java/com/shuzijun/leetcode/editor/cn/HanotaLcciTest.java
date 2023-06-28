package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;


class HanotaLcciTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final List<Integer> C = new ArrayList<>();
    solution.hanota(new ArrayList(Arrays.asList(3, 2, 1, 0)),
        new ArrayList<>(),
        C);
    then(C).containsExactly(3, 2, 1, 0);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public void hanota(List<Integer> A, List<Integer> B, List<Integer> C) {
      helper(A.size(), A, B, C);
    }

    private void helper(int size, List<Integer> origin, List<Integer> middle,
        List<Integer> target) {
      if (size == 1) {
        target.add(origin.remove(origin.size() - 1));
        return;
      }
      helper(size - 1, origin, target, middle);
      target.add(origin.remove(origin.size() - 1));
      helper(size - 1, middle, origin, target);
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
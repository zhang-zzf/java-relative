
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;


class PascalsTriangleTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final List<List<Integer>> lists = solution.generate(4);
        then(lists).hasSize(4);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public List<List<Integer>> generate(int numRows) {
            List<List<Integer>> ans = new ArrayList<>(numRows);
            ans.add(Arrays.asList(1));
            for (int i = 2; i <= numRows; i++) {
                List<Integer> curRow = new ArrayList<>(i);
                curRow.add(1);
                List<Integer> prevRow = ans.get(i - 2);
                for (int j = 1; j < i - 1; j++) {
                    curRow.add(prevRow.get(j - 1) + prevRow.get(j));
                }
                curRow.add(1);
                ans.add(curRow);
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}

package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
            if (numRows == 1) {
                return new ArrayList<List<Integer>>() {{
                    add(new ArrayList<Integer>() {{
                        add(1);
                    }});
                }};
            }
            final List<List<Integer>> lists = generate(numRows - 1);
            lists.add(generateCurRow(numRows, lists.get(lists.size() - 1)));
            return lists;
        }

        private List<Integer> generateCurRow(int numRows, List<Integer> prevRow) {
            List<Integer> curRow = new ArrayList<>(numRows);
            curRow.add(1);
            for (int i = 1; i < numRows - 1; i++) {
                curRow.add(prevRow.get(i - 1) + prevRow.get(i));
            }
            curRow.add(1);
            return curRow;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
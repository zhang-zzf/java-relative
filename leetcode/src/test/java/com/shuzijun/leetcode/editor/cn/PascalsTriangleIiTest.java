package com.shuzijun.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;


class PascalsTriangleIiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {

    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public List<Integer> getRow(int rowIndex) {
            if (rowIndex == 0) {
                return Arrays.asList(1);
            }
            return generateCurRow(rowIndex + 1, getRow(rowIndex - 1));
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
    // leetcode submit region end(Prohibit modification and deletion)


}
package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;


class TriangleTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int minimumTotal = solution.minimumTotal(Arrays.asList(
            Arrays.asList(2),
            Arrays.asList(3, 4),
            Arrays.asList(6, 5, 7),
            Arrays.asList(4, 1, 8, 3)
        ));
        then(minimumTotal).isEqualTo(11);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int minimumTotal(List<List<Integer>> triangle) {
            final int lng = triangle.size();
            int[] prev = new int[lng];
            prev[0] = triangle.get(0).get(0);
            int[] cur = new int[lng];
            for (int i = 1; i < lng; i++) {
                final List<Integer> list = triangle.get(i);
                for (int j = 0; j < list.size(); j++) {
                    int min = (j == list.size() - 1) ? prev[j - 1] : prev[j];
                    if (j - 1 >= 0) {
                        min = Math.min(min, prev[j - 1]);
                    }
                    cur[j] = list.get(j) + min;
                }
                // 滚动数组
                int[] tmp = prev;
                prev = cur;
                cur = tmp;
            }
            int ans = prev[0];
            for (int i = 0; i < lng; i++) {
                ans = Math.min(ans, prev[i]);
            }
            return ans;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
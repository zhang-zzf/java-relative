
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class UniquePathsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int paths = solution.uniquePaths(3, 7);
        then(paths).isEqualTo(28);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int uniquePaths(int m, int n) {
            int[] prev = new int[n];
            int[] cur = new int[n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    cur[j] = (j == 0 ? 1 : cur[j - 1] + prev[j]);
                }
                // 滚动数组
                int[] tmp = prev;
                prev = cur;
                cur = tmp;
            }
            return prev[n - 1];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
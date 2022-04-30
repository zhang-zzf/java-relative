
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
            // 多一个长度，不用 care 边界问题
            int[] prev = new int[n + 1];
            int[] cur = new int[n + 1];
            // init
            prev[1] = 1;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    cur[j + 1] = cur[j] + prev[j + 1];
                }
                int[] tmp = prev;
                prev = cur;
                cur = tmp;
            }
            return prev[n];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
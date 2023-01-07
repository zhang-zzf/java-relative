//给定一个由 0 和 1 组成的矩阵 mat ，请输出一个大小相同的矩阵，其中每一个格子是 mat 中对应位置元素到最近的 0 的距离。 
//
// 两个相邻元素间的距离为 1 。 
//
// 
//
// 示例 1： 
//
// 
//
// 
//输入：mat = [[0,0,0],[0,1,0],[0,0,0]]
//输出：[[0,0,0],[0,1,0],[0,0,0]]
// 
//
// 示例 2： 
//
// 
//
// 
//输入：mat = [[0,0,0],[0,1,0],[1,1,1]]
//输出：[[0,0,0],[0,1,0],[1,2,1]]
// 
//
// 
//
// 提示： 
//
// 
// m == mat.length 
// n == mat[i].length 
// 1 <= m, n <= 10⁴ 
// 1 <= m * n <= 10⁴ 
// mat[i][j] is either 0 or 1. 
// mat 中至少有一个 0 
// 
//
// Related Topics 广度优先搜索 数组 动态规划 矩阵 
// 👍 794 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.assertj.core.api.BDDAssertions.then;


public class Zero1MatrixTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        int[][] matrix = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0},
        };
        int[][] ans = solution.updateMatrix(matrix);
        then(ans).isDeepEqualTo(new int[][]{
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0},
        });
    }

    @Test
    void givenTestCase2_when_then() {
        int[][] matrix = {
                {0, 0, 0},
                {0, 1, 0},
                {1, 1, 1},
        };
        int[][] ans = solution.updateMatrix(matrix);
        then(ans).isDeepEqualTo(new int[][]{
                {0, 0, 0},
                {0, 1, 0},
                {1, 2, 1},
        });
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int[][] updateMatrix(int[][] mat) {
            int m = mat.length, n = mat[0].length;
            int[][] ans = new int[m][n];
            Queue<Integer> queue = new LinkedList<>();
            int[][] visited = new int[m][n];
            for (int r = 0; r < m; r++) {
                for (int c = 0; c < n; c++) {
                    if (mat[r][c] == 0) {
                        queue.offer(r * n + c);
                        visited[r][c] = 1;
                    }
                }
            }
            int level = 0;
            int[][] dirs = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
            while (!queue.isEmpty()) {
                int size = queue.size();
                while (size-- > 0) {
                    Integer cur = queue.poll();
                    int r = cur / n, c = cur % n;
                    for (int i = 0; i < dirs.length; i++) {
                        int nr = r + dirs[i][0], nc = c + dirs[i][1];
                        if (nr >= 0 && nr < m &
                                nc >= 0 && nc < n &&
                                mat[nr][nc] == 1 &&
                                visited[nr][nc] == 0) {
                            ans[nr][nc] = level + 1;
                            visited[nr][nc] = 1;
                            queue.offer(nr * n + nc);
                        }
                    }
                }
                level += 1;
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
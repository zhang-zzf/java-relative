// 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
//
// 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。 
//
// 此外，你可以假设该网格的四条边均被水包围。 
//
// 
//
// 示例 1： 
//
// 
// 输入：grid = [
//  ["1","1","1","1","0"],
//  ["1","1","0","1","0"],
//  ["1","1","0","0","0"],
//  ["0","0","0","0","0"]
//]
// 输出：1
// 
//
// 示例 2： 
//
// 
// 输入：grid = [
//  ["1","1","0","0","0"],
//  ["1","1","0","0","0"],
//  ["0","0","1","0","0"],
//  ["0","0","0","1","1"]
//]
// 输出：3
// 
//
// 
//
// 提示： 
//
// 
// m == grid.length 
// n == grid[i].length 
// 1 <= m, n <= 300 
// grid[i][j] 的值为 '0' 或 '1' 
// 
// Related Topics 深度优先搜索 广度优先搜索 并查集 数组 矩阵 👍 1522 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.LinkedList;
import java.util.Queue;
import org.junit.jupiter.api.Test;


public class NumberOfIslandsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int nums = solution.numIslands(new char[][]{
            new char[]{'1', '1', '1', '1', '1', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0',
                '1', '0', '1', '1'},
            new char[]{'0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0', '1', '1',
                '1', '1', '1', '0'},
            new char[]{'1', '0', '1', '1', '1', '0', '0', '1', '1', '0', '1', '1', '1', '1', '1', '1',
                '1', '1', '1', '1'},
            new char[]{'1', '1', '1', '1', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
                '1', '1', '1', '1'},
            new char[]{'1', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
                '1', '1', '1', '1'},
            new char[]{'1', '0', '1', '1', '1', '1', '1', '1', '0', '1', '1', '1', '0', '1', '1', '1',
                '0', '1', '1', '1'},
            new char[]{'0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0', '1', '1', '0',
                '1', '1', '1', '1'},
            new char[]{'1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0', '1', '1', '1',
                '1', '0', '1', '1'},
            new char[]{'1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0', '1', '1', '1', '1', '1',
                '1', '1', '1', '1'},
            new char[]{'1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
                '1', '1', '1', '1'},
            new char[]{'0', '1', '1', '1', '1', '1', '1', '1', '0', '1', '1', '1', '1', '1', '1', '1',
                '1', '1', '1', '1'},
            new char[]{'1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
                '1', '1', '1', '1'},
            new char[]{'1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
                '1', '1', '1', '1'},
            new char[]{'1', '1', '1', '1', '1', '0', '1', '1', '1', '1', '1', '1', '1', '0', '1', '1',
                '1', '1', '1', '1'},
            new char[]{'1', '0', '1', '1', '1', '1', '1', '0', '1', '1', '1', '0', '1', '1', '1', '1',
                '0', '1', '1', '1'},
            new char[]{'1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0', '1', '1', '1',
                '1', '1', '1', '0'},
            new char[]{'1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0', '1', '1',
                '1', '1', '0', '0'},
            new char[]{'1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
                '1', '1', '1', '1'},
            new char[]{'1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
                '1', '1', '1', '1'},
            new char[]{'1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
                '1', '1', '1', '1'},
        });
        then(nums).isEqualTo(1);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int numIslands(char[][] grid) {
            int ret = 0;
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] == '1') {
                        ret += 1;
                        // 抹除整个相连的岛屿
                        dfs(grid, i, j);
                    }
                }
            }
            return ret;
        }

        private void dfs(char[][] grid, int i, int j) {
            if (i < 0 || i >= grid.length
                || j < 0 || j >= grid[i].length
                || grid[i][j] == '0') {
                return;
            }
            grid[i][j] = '0';
            dfs(grid, i - 1, j);
            dfs(grid, i, j + 1);
            dfs(grid, i + 1, j);
            dfs(grid, i, j - 1);
        }

        private void bfsEraseEntireIsland(char[][] grid, int i, int j) {
            Queue<int[]> queue = new LinkedList<>();
            grid[i][j] = '0';
            queue.add(new int[]{i, j});
            while (!queue.isEmpty()) {
                final int[] poll = queue.poll();
                i = poll[0];
                j = poll[1];
                if (i > 0 && grid[i - 1][j] == '1') {
                    grid[i - 1][j] = '0';
                    queue.add(new int[]{i - 1, j});
                }
                if (j + 1 < grid[i].length && grid[i][j + 1] == '1') {
                    grid[i][j + 1] = '0';
                    queue.add(new int[]{i, j + 1});
                }
                if (i + 1 < grid.length && grid[i + 1][j] == '1') {
                    grid[i + 1][j] = '0';
                    queue.add(new int[]{i + 1, j});
                }
                if (j > 0 && grid[i][j - 1] == '1') {
                    grid[i][j - 1] = '0';
                    queue.add(new int[]{i, j - 1});
                }
            }
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
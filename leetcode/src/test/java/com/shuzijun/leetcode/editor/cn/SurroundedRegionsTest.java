//给你一个 m x n 的矩阵 board ，由若干字符 'X' 和 'O' ，找到所有被 'X' 围绕的区域，并将这些区域里所有的 'O' 用 'X' 填充
//。
// 
// 
// 
//
// 示例 1： 
//
// 
//输入：board = [["X","X","X","X"],["X","O","O","X"],["X","X","O","X"],["X","O",
//"X","X"]]
//输出：[["X","X","X","X"],["X","X","X","X"],["X","X","X","X"],["X","O","X","X"]]
//解释：被围绕的区间不会存在于边界上，换句话说，任何边界上的 'O' 都不会被填充为 'X'。 任何不在边界上，或不与边界上的 'O' 相连的 'O' 最终都
//会被填充为 'X'。如果两个元素在水平或垂直方向相邻，则称它们是“相连”的。
// 
//
// 示例 2： 
//
// 
//输入：board = [["X"]]
//输出：[["X"]]
// 
//
// 
//
// 提示： 
//
// 
// m == board.length 
// n == board[i].length 
// 1 <= m, n <= 200 
// board[i][j] 为 'X' 或 'O' 
// 
// 
// 
// Related Topics 深度优先搜索 广度优先搜索 并查集 数组 矩阵 👍 717 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.LinkedList;
import java.util.Queue;
import org.junit.jupiter.api.Test;


public class SurroundedRegionsTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    char[][] chars = {
        new char[]{'X', 'X', 'X', 'X'},
        new char[]{'X', 'O', 'O', 'X'},
        new char[]{'X', 'X', 'O', 'X'},
        new char[]{'X', 'O', 'X', 'X'},
    };
    solution.solve(chars);
    then(chars).isNotEmpty();
    chars = new char[][]{
        new char[]{'O', 'O', 'O'},
        new char[]{'O', 'O', 'O'},
        new char[]{'O', 'O', 'O'},
    };
    solution.solve(chars);
    then(chars).isNotEmpty();
  }

  //leetcode submit region begin(Prohibit modification and deletion)

  class Solution {

    public void solve(char[][] board) {
      for (int i = 0; i < board[0].length; i++) {
        dfsMarkOToM(board, 0, i);
        dfsMarkOToM(board, board.length - 1, i);
      }
      for (int i = 0; i < board.length; i++) {
        dfsMarkOToM(board, i, 0);
        dfsMarkOToM(board, i, board[0].length - 1);
      }
      for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[i].length; j++) {
          if (board[i][j] == 'M') {
            board[i][j] = 'O';
          } else if (board[i][j] == 'O') {
            board[i][j] = 'X';
          }
        }
      }
    }

    private void dfsMarkOToM(char[][] board, int i, int j) {
      if (i < 0 || j < 0 || i >= board.length || j >= board[0].length) {
        return;
      }
      if (board[i][j] != 'O') {
        return;
      }
      board[i][j] = 'M';
      dfsMarkOToM(board, i - 1, j);
      dfsMarkOToM(board, i, j + 1);
      dfsMarkOToM(board, i + 1, j);
      dfsMarkOToM(board, i, j - 1);
    }

    private void bfsMarkOToM(char[][] board, int i, int j) {
      if (board[i][j] != 'O') {
        return;
      }
      Queue<int[]> queue = new LinkedList<>();
      queue.add(new int[]{i, j});
      board[i][j] = 'M';
      while (!queue.isEmpty()) {
        final int[] poll = queue.poll();
        final int r = poll[0], c = poll[1];
        if (r > 0 && board[r - 1][c] == 'O') {
          board[r - 1][c] = 'M';
          queue.add(new int[]{r - 1, c});
        }
        if (c + 1 < board[r].length && board[r][c + 1] == 'O') {
          board[r][c + 1] = 'M';
          queue.add(new int[]{r, c + 1});
        }
        if (r + 1 < board.length && board[r + 1][c] == 'O') {
          board[r + 1][c] = 'M';
          queue.add(new int[]{r + 1, c});
        }
        if (c > 0 && board[r][c - 1] == 'O') {
          board[r][c - 1] = 'M';
          queue.add(new int[]{r, c - 1});
        }
      }
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
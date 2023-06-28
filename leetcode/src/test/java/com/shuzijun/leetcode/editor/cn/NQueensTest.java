//n 皇后问题 研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。 
//
// 给你一个整数 n ，返回所有不同的 n 皇后问题 的解决方案。 
//
// 
// 
// 每一种解法包含一个不同的 n 皇后问题 的棋子放置方案，该方案中 'Q' 和 '.' 分别代表了皇后和空位。 
//
// 
//
// 示例 1： 
//
// 
//输入：n = 4
//输出：[[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
//解释：如上图所示，4 皇后问题存在两个不同的解法。
// 
//
// 示例 2： 
//
// 
//输入：n = 1
//输出：[["Q"]]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= n <= 9 
// 
// 
// 
// Related Topics 数组 回溯 👍 1131 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


public class NQueensTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final List<List<String>> lists = solution.solveNQueens(4);
    then(lists).hasSize(2);
    final List<List<String>> lists1 = solution.solveNQueens(1);
    final List<List<String>> lists2 = solution.solveNQueens(2);
    final List<List<String>> lists3 = solution.solveNQueens(3);
    final List<List<String>> lists8 = solution.solveNQueens(8);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    /**
     * 解题思路：https://www.yuque.com/u1147067/vzaha9/zdbwvt#shXau
     */
    public List<List<String>> solveNQueens(int n) {
      List<List<String>> ret = new ArrayList<>();
      List<Integer> selectedColumn = new ArrayList<>(n);
      backTrack(n, 0, selectedColumn, ret);
      return ret;
    }

    private void backTrack(int n, int row, List<Integer> selectedColumn, List<List<String>> ret) {
      if (selectedColumn.size() == n) {
        List<String> solution = new ArrayList<>();
        // 按行遍历棋盘
        for (int i = 0; i < n; i++) {
          final Integer column = selectedColumn.get(i);
          StringBuilder buf = new StringBuilder(n);
          // 按列构建字符串
          for (int j = 0; j < n; j++) {
            buf.append(column == j ? "Q" : ".");
          }
          solution.add(buf.toString());
        }
        // 添加
        ret.add(solution);
        return;
      }
      // 选择
      // 按棋盘当前行遍历列
      for (int i = 0; i < n; i++) {
        int curRow = row;
        int curColumn = i;
        // 判断能不能攻击已存在的皇后
        boolean canAttachExistQueue = false;
        for (int j = 0; j < selectedColumn.size(); j++) {
          int eRow = j, eColumn = selectedColumn.get(j);
          if (curRow == eRow
              || curColumn == eColumn
              || Math.abs(eRow - curRow) == Math.abs(eColumn - curColumn)) {
            canAttachExistQueue = true;
            break;
          }
        }
        if (canAttachExistQueue) {
          continue;
        }
        // 选择列
        selectedColumn.add(curColumn);
        backTrack(n, row + 1, selectedColumn, ret);
        // 撤销选择
        selectedColumn.remove(selectedColumn.size() - 1);
      }
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
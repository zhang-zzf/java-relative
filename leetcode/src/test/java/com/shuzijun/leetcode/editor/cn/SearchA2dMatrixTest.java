//编写一个高效的算法来判断 m x n 矩阵中，是否存在一个目标值。该矩阵具有如下特性： 
//
// 
// 每行中的整数从左到右按升序排列。 
// 每行的第一个整数大于前一行的最后一个整数。 
// 
//
// 
//
// 示例 1： 
// 
// 
//输入：matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3
//输出：true
// 
//
// 示例 2： 
// 
// 
//输入：matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 13
//输出：false
// 
//
// 
//
// 提示： 
//
// 
// m == matrix.length 
// n == matrix[i].length 
// 1 <= m, n <= 100 
// -10⁴ <= matrix[i][j], target <= 10⁴ 
// 
//
// Related Topics 数组 二分查找 矩阵 
// 👍 762 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class SearchA2dMatrixTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    int[][] matrix = {{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
    then(solution.searchMatrix(matrix, 3)).isTrue();
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public boolean searchMatrix(int[][] matrix, int target) {
      boolean ans = false;
      int m = matrix.length;
      if (m == 0) {
        return ans;
      }
      int n = matrix[0].length;
      int left = 0, right = m * n - 1;
      while (left <= right) {
        int mid = left + ((right - left) >> 1);
        int i = mid / n, j = mid % n;
        if (target == matrix[i][j]) {
          ans = true;
          break;
        } else if (target > matrix[i][j]) {
          left = mid + 1;
        } else {
          right = mid - 1;
        }
      }
      return ans;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
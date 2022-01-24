//给你一个 n x n 矩阵 matrix ，其中每行和每列元素均按升序排序，找到矩阵中第 k 小的元素。 
//请注意，它是 排序后 的第 k 小元素，而不是第 k 个 不同 的元素。 
//
// 
//
// 示例 1： 
//
// 
//输入：matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8
//输出：13
//解释：矩阵中的元素为 [1,5,9,10,11,12,13,13,15]，第 8 小元素是 13
// 
//
// 示例 2： 
//
// 
//输入：matrix = [[-5]], k = 1
//输出：-5
// 
//
// 
//
// 提示： 
//
// 
// n == matrix.length 
// n == matrix[i].length 
// 1 <= n <= 300 
// -10⁹ <= matrix[i][j] <= 10⁹ 
// 题目数据 保证 matrix 中的所有行和列都按 非递减顺序 排列 
// 1 <= k <= n² 
// 
// Related Topics 数组 二分查找 矩阵 排序 堆（优先队列） 👍 744 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class KthSmallestElementInASortedMatrixTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int kthSmallest = solution.kthSmallest(new int[][]{
                        new int[]{1, 5, 9},
                        new int[]{10, 11, 13},
                        new int[]{12, 13, 15}},
                8);
        then(kthSmallest).isEqualTo(13);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int kthSmallest(int[][] matrix, int k) {
            int l = matrix[0][0], r = matrix[matrix.length - 1][matrix.length - 1];
            while (l < r) {
                int mid = l + ((r - l) >> 1);
                int cnt = countSmall(mid, matrix);
                if (cnt < k) {
                    // 在右侧
                    l = mid + 1;
                } else {
                    r = mid;
                }
            }
            return l;
        }

        private int countSmall(int pivot, int[][] matrix) {
            int r = matrix.length - 1, c = 0;
            int ret = 0;
            while (r >= 0) {
                while (c < matrix[0].length && matrix[r][c] <= pivot) {
                    c += 1;
                }
                ret += c;
                r -= 1;
            }
            return ret;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
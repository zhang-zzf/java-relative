//给你一个 m * n 的矩阵 mat，以及一个整数 k ，矩阵中的每一行都以非递减的顺序排列。 
//
// 你可以从每一行中选出 1 个元素形成一个数组。返回所有可能数组中的第 k 个 最小 数组和。 
//
// 
//
// 示例 1： 
//
// 输入：mat = [[1,3,11],[2,4,6]], k = 5
//输出：7
//解释：从每一行中选出一个元素，前 k 个和最小的数组分别是：
//[1,2], [1,4], [3,2], [3,4], [1,6]。其中第 5 个的和是 7 。  
//
// 示例 2： 
//
// 输入：mat = [[1,3,11],[2,4,6]], k = 9
//输出：17
// 
//
// 示例 3： 
//
// 输入：mat = [[1,10,10],[1,4,5],[2,3,6]], k = 7
//输出：9
//解释：从每一行中选出一个元素，前 k 个和最小的数组分别是：
//[1,1,2], [1,1,3], [1,4,2], [1,4,3], [1,1,6], [1,5,2], [1,5,3]。其中第 7 个的和是 9 。 
// 
//
// 示例 4： 
//
// 输入：mat = [[1,1,10],[2,2,9]], k = 7
//输出：12
// 
//
// 
//
// 提示： 
//
// 
// m == mat.length 
// n == mat.length[i] 
// 1 <= m, n <= 40 
// 1 <= k <= min(200, n ^ m) 
// 1 <= mat[i][j] <= 5000 
// mat[i] 是一个非递减数组 
// 
// Related Topics 数组 二分查找 矩阵 堆（优先队列） 👍 88 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.PriorityQueue;

import static org.assertj.core.api.BDDAssertions.then;


public class FindTheKthSmallestSumOfAMatrixWithSortedRowsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int kthSmallest = solution.kthSmallest(new int[][]{
                new int[]{1, 10, 10},
                new int[]{1, 4, 5},
                new int[]{2, 3, 6},

        }, 7);
        then(kthSmallest).isEqualTo(9);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int kthSmallest(int[][] mat, int k) {
            Item[] ret = new Item[k];
            PriorityQueue<Item> pq = new PriorityQueue<>();
            // 第一列
            int[] data = new int[mat.length];
            int[] idxOfRow = new int[mat.length];
            for (int i = 0; i < mat.length; i++) {
                data[i] = mat[i][0];
                idxOfRow[i] = 0;
            }
            pq.add(new Item(data, idxOfRow));
            while (!pq.isEmpty() && k-- > 0) {
                Item poll = pq.poll();
                while (pq.size() > 0 && poll.compareTo(pq.peek()) == 0) {
                    poll = pq.poll();
                }
                ret[k] = poll;
                for (int i = 0; i < mat.length; i++) {
                    final int newColumn = poll.idxOfRow[i] + 1;
                    if (newColumn < mat[i].length) {
                        final int[] newData = Arrays.copyOf(poll.data, poll.data.length);
                        newData[i] = mat[i][newColumn];
                        final int[] newIdx = Arrays.copyOf(poll.idxOfRow, poll.idxOfRow.length);
                        newIdx[i] = newColumn;
                        pq.add(new Item(newData, newIdx));
                    }
                }
            }
            return ret[0] == null ? -1 : ret[0].sum;
        }

    }

    class Item implements Comparable<Item> {

        int[] data;
        int sum;
        int[] idxOfRow;

        public Item(int[] data, int[] idxOfRow) {
            this.data = data;
            for (int d : data) {
                this.sum += d;
            }
            this.idxOfRow = idxOfRow;
        }

        @Override
        public int compareTo(Item o) {
            int c = this.sum - o.sum;
            if (c != 0) {
                return c;
            }
            // 核心去重逻辑
            for (int i = 0; i < this.idxOfRow.length; i++) {
                c = this.idxOfRow[i] - o.idxOfRow[i];
                if (c != 0) {
                    return c;
                }
            }
            return 0;
        }

    }

//leetcode submit region end(Prohibit modification and deletion)

}

//设计一个算法，找出数组中最小的k个数。以任意顺序返回这k个数均可。 
//
// 示例： 
//
// 输入： arr = [1,3,5,7,2,4,6,8], k = 4
//输出： [1,2,3,4]
// 
//
// 提示： 
//
// 
// 0 <= len(arr) <= 100000 
// 0 <= k <= min(100000, len(arr)) 
// 
// Related Topics 数组 分治 快速选择 排序 堆（优先队列） 👍 178 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.BDDAssertions.then;


public class SmallestKLcciTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        // [1,3,5,7,2,4,6,8]
        // 4
        then(solution.smallestK(new int[]{1, 3, 5, 7, 2, 4, 6, 8}, 4)).hasSize(4);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int[] smallestK(int[] arr, int k) {
            if (arr.length == 0 || k == 0) {
                return new int[0];
            }
            quickSortK(arr, k, 0, arr.length - 1);
            return Arrays.copyOf(arr, k);
        }

        private void quickSortK(int[] arr, int k, int left, int right) {
            int pivotIdx = partition(arr, left, right);
            if (pivotIdx == k - 1 || pivotIdx == k || left >= right) {
                return;
            } else if (pivotIdx > k) {
                quickSortK(arr, k, left, pivotIdx - 1);
            } else {
                quickSortK(arr, k, pivotIdx + 1, right);
            }

        }

        private int partition(int[] arr, int left, int right) {
            int pivot = arr[right];
            int ptr = left - 1;
            for (int i = left; i < right; i++) {
                if (arr[i] < pivot) {
                    swap(arr, ++ptr, i);
                }
            }
            swap(arr, ptr + 1, right);
            return ptr + 1;
        }

        private void swap(int[] arr, int i, int i1) {
            int tmp = arr[i];
            arr[i] = arr[i1];
            arr[i1] = tmp;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
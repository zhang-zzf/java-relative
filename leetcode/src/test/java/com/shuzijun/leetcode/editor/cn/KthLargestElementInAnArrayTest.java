//给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。 
//
// 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。 
//
// 
//
// 示例 1: 
//
// 
//输入: [3,2,1,5,6,4] 和 k = 2
//输出: 5
// 
//
// 示例 2: 
//
// 
//输入: [3,2,3,1,2,4,5,5,6] 和 k = 4
//输出: 4 
//
// 
//
// 提示： 
//
// 
// 1 <= k <= nums.length <= 10⁴ 
// -10⁴ <= nums[i] <= 10⁴ 
// 
// Related Topics 数组 分治 快速选择 排序 堆（优先队列） 👍 1485 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class KthLargestElementInAnArrayTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2)).isEqualTo(5);
        then(solution.findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4)).isEqualTo(4);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int findKthLargest(int[] nums, int k) {
            final int numsIdx = nums.length - k;
            quickSort(nums, numsIdx, 0, nums.length - 1);
            return nums[numsIdx];
        }

        private void quickSort(int[] nums, int k, int left, int right) {
            int pivotIdx = partition(nums, left, right);
            if (pivotIdx == k || left >= right) {
                return;
            } else if (pivotIdx < k) {
                quickSort(nums, k, pivotIdx + 1, right);
            } else {
                quickSort(nums, k, left, pivotIdx - 1);
            }
        }

        private int partition(int[] nums, int left, int right) {
            int pivotIdx = right;
            // nums[left..ptr] 都是小于 pivot 的数
            int ptr = left - 1;
            for (int i = left; i < right; i++) {
                if (nums[i] < nums[pivotIdx]) {
                    swap(nums, ++ptr, i);
                }
            }
            swap(nums, ptr + 1, right);
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
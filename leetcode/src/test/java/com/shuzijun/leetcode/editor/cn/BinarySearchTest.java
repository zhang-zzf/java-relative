
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


public class BinarySearchTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {

    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int search(int[] nums, int target) {
            // 定义值域 [-1, nums.length-1]
            int ret = -1;
            int left = 0, right = nums.length - 1;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                int value = getV(nums, mid);
                if (value == target) {
                    ret = mid;
                    break;
                } else if (value < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            return ret;
        }

        private int getV(int[] nums, int idx) {
            return nums[idx];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
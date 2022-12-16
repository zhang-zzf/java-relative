
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class BinarySearchTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        int[] nums = new int[]{-1, 0, 3, 5, 9, 12};
        then(solution.search(nums, 9)).isEqualTo(4);
    }

    @Test
    void givenNotExists_when_thenReturnNegativeOne() {
        int[] nums = new int[]{-1, 0, 3, 5, 9, 12};
        then(solution.search(nums, 2)).isEqualTo(-1);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int search(int[] nums, int target) {
            int ans = -1;
            int left = 0, right = nums.length - 1;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                int val = getV(nums, mid);
                if (val == target) {
                    ans = mid;
                    break;
                } else if (target < val) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            return ans;
        }

        private int getV(int[] nums, int mid) {
            return nums[mid];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
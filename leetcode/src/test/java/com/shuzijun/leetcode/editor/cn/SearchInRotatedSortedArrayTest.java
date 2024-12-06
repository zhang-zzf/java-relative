package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class SearchInRotatedSortedArrayTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int[] nums = {4, 5, 6, 7, 0, 1, 2};
        then(solution.search(nums, 4)).isEqualTo(0);
        then(solution.search(nums, 5)).isEqualTo(1);
        then(solution.search(nums, 7)).isEqualTo(3);
        then(solution.search(nums, 0)).isEqualTo(4);
        then(solution.search(nums, 1)).isEqualTo(5);
        then(solution.search(nums, 2)).isEqualTo(6);
        then(solution.search(nums, 3)).isEqualTo(-1);
    }


    @Test
    void
    givenFailedCase1_when_then() {
        // mid == left 时 mid<->left 是有序的
        then(solution.search(new int[]{3, 1}, 1)).isEqualTo(1);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int search(int[] nums, int target) {
            int ans = -1;
            int left = 0, right = nums.length - 1;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                int val = nums[mid];
                if (val == target) {
                    ans = mid;
                    break;
                }
                // when mid==left then nums[left]..nums[mid] is sorted
                if (nums[left] <= val) {
                    // left <-> mid is sorted
                    if (val > target && nums[left] <= target) {
                        right = mid - 1;
                    }
                    else {
                        left = mid + 1;
                    }
                }
                else {
                    // mid <-> right is sorted
                    if (val < target && nums[right] >= target) {
                        left = mid + 1;
                    }
                    else {
                        right = mid - 1;
                    }
                }
            }
            return ans;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
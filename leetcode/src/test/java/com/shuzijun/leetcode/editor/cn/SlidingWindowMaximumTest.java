//给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位
//。 
//
// 返回 滑动窗口中的最大值 。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [1,3,-1,-3,5,3,6,7], k = 3
//输出：[3,3,5,5,6,7]
//解释：
//滑动窗口的位置                最大值
//---------------               -----
//[1  3  -1] -3  5  3  6  7       3
// 1 [3  -1  -3] 5  3  6  7       3
// 1  3 [-1  -3  5] 3  6  7       5
// 1  3  -1 [-3  5  3] 6  7       5
// 1  3  -1  -3 [5  3  6] 7       6
// 1  3  -1  -3  5 [3  6  7]      7
// 
//
// 示例 2： 
//
// 
//输入：nums = [1], k = 1
//输出：[1]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 10⁵ 
// -10⁴ <= nums[i] <= 10⁴ 
// 1 <= k <= nums.length 
// 
//
// Related Topics 队列 数组 滑动窗口 单调队列 堆（优先队列） 
// 👍 2349 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.BDDAssertions.then;


public class SlidingWindowMaximumTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3))
                .containsExactly(3, 3, 5, 5, 6, 7);
        then(solution.maxSlidingWindow(new int[]{1}, 1))
                .containsExactly(1);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int[] maxSlidingWindow(int[] nums, int k) {
            List<int[]> ans = new ArrayList<>(nums.length);
            for (int rightIdx = k - 1; rightIdx < nums.length; rightIdx++) {
                if (ans.isEmpty()) {
                    ans.add(findMax(nums, rightIdx - k + 1, rightIdx));
                } else {
                    int[] prevMax = ans.get(ans.size() - 1);
                    int num = nums[rightIdx];
                    if (num > prevMax[1]) {
                        ans.add(new int[]{rightIdx, num});
                    } else if (prevMax[0] > rightIdx - k) {
                        ans.add(prevMax);
                    } else {
                        ans.add(findMax(nums, rightIdx - k + 1, rightIdx));
                    }
                }
            }
            return ans.stream().mapToInt(arr -> arr[1]).toArray();
        }

        private int[] findMax(int[] nums, int leftIdx, int rightIdx) {
            int max = nums[leftIdx];
            int maxIdx = leftIdx;
            for (int j = leftIdx; j <= rightIdx; j++) {
                if (nums[j] > max) {
                    max = nums[j];
                    maxIdx = j;
                }
            }
            return new int[]{maxIdx, max};
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
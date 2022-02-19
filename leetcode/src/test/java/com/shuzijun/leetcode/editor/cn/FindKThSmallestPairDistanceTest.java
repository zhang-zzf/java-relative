//给定一个整数数组，返回所有数对之间的第 k 个最小距离。一对 (A, B) 的距离被定义为 A 和 B 之间的绝对差值。 
//
// 示例 1: 
//
// 
//输入：
//nums = [1,3,1]
//k = 1
//输出：0 
//解释：
//所有数对如下：
//(1,3) -> 2
//(1,1) -> 0
//(3,1) -> 2
//因此第 1 个最小距离的数对是 (1,1)，它们之间的距离为 0。
// 
//
// 提示: 
//
// 
// 2 <= len(nums) <= 10000. 
// 0 <= nums[i] < 1000000. 
// 1 <= k <= len(nums) * (len(nums) - 1) / 2. 
// 
// Related Topics 数组 双指针 二分查找 排序 👍 226 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.BDDAssertions.then;


public class FindKThSmallestPairDistanceTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int ret = solution.smallestDistancePair(new int[]{62, 100, 4}, 2);
        then(ret).isEqualTo(58);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int smallestDistancePair(int[] nums, int k) {
            Arrays.sort(nums);
            // 值域 [0...nums[nums.length-1] - nums[0]]
            int left = 0, right = nums[nums.length - 1] - nums[0];
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                int cnt = getLessEqualThan(nums, mid);
                if (cnt < k) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            return left;
        }

        private int getLessEqualThan(int[] nums, int pivot) {
            int ret = 0;
            int left = 0;
            for (int right = 0; right < nums.length; right++) {
                while (nums[right] - nums[left] > pivot) {
                    left++;
                }
                // 小于等于 pivot 的距离对
                ret += (right - left);
            }
            return ret;
        }

    }

//leetcode submit region end(Prohibit modification and deletion)


}
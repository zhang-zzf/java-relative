// 给定一个整数数组，返回所有数对之间的第 k 个最小距离。一对 (A, B) 的距离被定义为 A 和 B 之间的绝对差值。
//
// 示例 1: 
//
// 
// 输入：
// nums = [1,3,1]
// k = 1
// 输出：0
// 解释：
// 所有数对如下：
//(1,3) -> 2
//(1,1) -> 0
//(3,1) -> 2
// 因此第 1 个最小距离的数对是 (1,1)，它们之间的距离为 0。
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

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;
import org.junit.jupiter.api.Test;


public class FindKThSmallestPairDistanceTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int ret = solution.smallestDistancePair(new int[]{1, 3, 1}, 1);
        then(ret).isEqualTo(0);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int smallestDistancePair(int[] nums, int k) {
            Arrays.sort(nums);
            int l = nums[1] - nums[0], r = nums[nums.length - 1] - nums[0];
            while (l <= r) {
                int m = l + ((r - l) >> 1);
                // <= m 的距离对的个数
                int cnt = getCnt(nums, m);
                if (cnt < k) {
                    l = m + 1;
                }
                else {
                    r = m - 1;
                }
            }
            // 分析 l=r 后根据动指针的条件判断
            return l;
        }

        private int getCnt(int[] nums, int m) {
            int ans = 0;
            int left = 0, right = 0;
            for (; right < nums.length; right++) {
                while (nums[right] - nums[left] > m) {
                    left += 1;
                }
                ans += (right - left);
            }
            return ans;
        }

    }

    // leetcode submit region end(Prohibit modification and deletion)


}
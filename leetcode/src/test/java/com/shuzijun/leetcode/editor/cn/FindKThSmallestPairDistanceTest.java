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
import java.util.Comparator;
import java.util.PriorityQueue;

import static org.assertj.core.api.BDDAssertions.then;


public class FindKThSmallestPairDistanceTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int ret = solution.smallestDistancePair(new int[]{1, 8, 4, 7}, 2);
        then(ret).isEqualTo(3);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int smallestDistancePair(int[] nums, int k) {
            Arrays.sort(nums);
            // 大堆
            PriorityQueue<Integer> pq = new PriorityQueue<>(k + 1, Comparator.reverseOrder());
            for (int i = 0; i < nums.length; i++) {
                for (int j = i + 1; j < nums.length; j++) {
                    if (pq.size() == k && pq.peek() < nums[j] - nums[i]) {
                        // 跳出一层循环
                        break;
                    }
                    pq.add(nums[j] - nums[i]);
                    if (pq.size() > k) {
                        pq.poll();
                    }
                }
            }
            return pq.poll();
        }


    }
//leetcode submit region end(Prohibit modification and deletion)


}
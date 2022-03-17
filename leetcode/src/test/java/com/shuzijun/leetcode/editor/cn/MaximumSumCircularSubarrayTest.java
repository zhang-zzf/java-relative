
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;

import static org.assertj.core.api.BDDAssertions.then;


class MaximumSumCircularSubarrayTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.maxSubarraySumCircular(new int[]{5, -3, 5})).isEqualTo(10);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxSubarraySumCircular(int[] nums) {
            final int N = nums.length;
            // 前缀和
            int[] sum = new int[N * 2 + 1];
            for (int i = 0; i < N * 2; i++) {
                sum[i + 1] = sum[i] + nums[i % N];
            }
            int ans = nums[0];
            Deque<Integer> deque = new LinkedList<>();
            deque.addLast(0);
            for (int i = 1; i < sum.length; i++) {
                // 注意：这里必须是 >
                if (i - N > deque.peekFirst()) {
                    deque.pollFirst();
                }
                ans = Math.max(ans, sum[i] - sum[deque.peekFirst()]);
                // 队列中的 K 个元素若比当前元素大，没有保留的必要
                // 变相的优先队列，但同时拥有2个纬度的数据
                // 队列中存储的是索引值，索引值对应的sum[i]是严格递增排序
                while (!deque.isEmpty() && sum[i] <= sum[deque.peekLast()]) {
                    deque.pollLast();
                }
                deque.addLast(i);
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
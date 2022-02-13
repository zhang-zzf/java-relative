//给你一个整数 n ，请你找出并返回第 n 个 丑数 。 
//
// 丑数 就是只包含质因数 2、3 和/或 5 的正整数。 
//
// 
//
// 示例 1： 
//
// 
//输入：n = 10
//输出：12
//解释：[1, 2, 3, 4, 5, 6, 8, 9, 10, 12] 是由前 10 个丑数组成的序列。
// 
//
// 示例 2： 
//
// 
//输入：n = 1
//输出：1
//解释：1 通常被视为丑数。
// 
//
// 
//
// 提示： 
//
// 
// 1 <= n <= 1690 
// 
// Related Topics 哈希表 数学 动态规划 堆（优先队列） 👍 839 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.PriorityQueue;


public class UglyNumberIiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {

    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int nthUglyNumber(int n) {
            int[] dp = new int[n];
            dp[0] = 1;
            int[] primes = {2, 3, 5};
            PriorityQueue<int[]> pq = new PriorityQueue<>(primes.length,
                    Comparator.comparingInt(a -> a[0]));
            for (int i = 0; i < primes.length; i++) {
                pq.add(new int[]{primes[i], i, 0});
            }
            for (int i = 1; i < n; i++) {
                final int[] poll = pq.poll();
                dp[i] = poll[0];
                pq.add(new int[]{primes[poll[1]] * dp[poll[2] + 1], poll[1], poll[2] + 1});
                if (dp[i] == dp[i - 1]) {
                    i -= 1;
                }
            }
            return dp[n - 1];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
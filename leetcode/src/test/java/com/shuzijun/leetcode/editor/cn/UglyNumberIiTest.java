// 给你一个整数 n ，请你找出并返回第 n 个 丑数 。
//
// 丑数 就是只包含质因数 2、3 和/或 5 的正整数。 
//
// 
//
// 示例 1： 
//
// 
// 输入：n = 10
// 输出：12
// 解释：[1, 2, 3, 4, 5, 6, 8, 9, 10, 12] 是由前 10 个丑数组成的序列。
// 
//
// 示例 2： 
//
// 
// 输入：n = 1
// 输出：1
// 解释：1 通常被视为丑数。
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

import static org.assertj.core.api.BDDAssertions.then;

import java.util.PriorityQueue;
import org.junit.jupiter.api.Test;


public class UglyNumberIiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.nthUglyNumber(10)).isEqualTo(12);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int nthUglyNumber(int n) {
            int[] ugly = new int[n];
            ugly[0] = 1;
            PriorityQueue<Item> pq = new PriorityQueue<>(3);
            pq.add(new Item(0, 2, 2));
            pq.add(new Item(0, 3, 3));
            pq.add(new Item(0, 5, 5));
            for (int i = 1; i < n; i++) {
                final Item min = pq.poll();
                ugly[i] = min.ugly;
                pq.add(new Item(min.uglyIndex + 1, min.prime, ugly[min.uglyIndex + 1] * min.prime));
                if (min.compareTo(pq.peek()) == 0) {
                    i -= 1;
                }
            }
            return ugly[n - 1];
        }

        class Item implements Comparable<Item> {

            int uglyIndex;
            int prime;
            int ugly;

            public Item(int uglyIndex, int prime, int ugly) {
                this.uglyIndex = uglyIndex;
                this.prime = prime;
                this.ugly = ugly;
            }

            @Override
            public int compareTo(Item o) {
                return this.ugly - o.ugly;
            }

        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
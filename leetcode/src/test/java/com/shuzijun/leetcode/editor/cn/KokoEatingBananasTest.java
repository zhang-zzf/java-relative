// 珂珂喜欢吃香蕉。这里有 N 堆香蕉，第 i 堆中有 piles[i] 根香蕉。警卫已经离开了，将在 H 小时后回来。
//
// 珂珂可以决定她吃香蕉的速度 K （单位：根/小时）。每个小时，她将会选择一堆香蕉，从中吃掉 K 根。如果这堆香蕉少于 K 根，她将吃掉这堆的所有香蕉，然后
// 这一小时内不会再吃更多的香蕉。
//
// 珂珂喜欢慢慢吃，但仍然想在警卫回来前吃掉所有的香蕉。 
//
// 返回她可以在 H 小时内吃掉所有香蕉的最小速度 K（K 为整数）。 
//
// 
//
// 
// 
//
// 示例 1： 
//
// 输入: piles = [3,6,7,11], H = 8
// 输出: 4
// 
//
// 示例 2： 
//
// 输入: piles = [30,11,23,4,20], H = 5
// 输出: 30
// 
//
// 示例 3： 
//
// 输入: piles = [30,11,23,4,20], H = 6
// 输出: 23
// 
//
// 
//
// 提示： 
//
// 
// 1 <= piles.length <= 10^4 
// piles.length <= H <= 10^9 
// 1 <= piles[i] <= 10^9 
// 
// Related Topics 数组 二分查找 👍 259 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class KokoEatingBananasTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.minEatingSpeed(new int[]{3, 6, 7, 11}, 8))
            .isEqualTo(4);
        then(solution.minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 5))
            .isEqualTo(30);
        then(solution.minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 6))
            .isEqualTo(23);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int minEatingSpeed(int[] piles, int h) {
            int max = piles[0];
            for (int i = 0; i < piles.length; i++) {
                if (piles[i] > max) {
                    max = piles[i];
                }
            }
            // 值域空间 [1,max]
            int l = 1, r = max;
            while (l < r) {
                int m = l + ((r - l) >> 1);
                if (eatAllWithSpeed(piles, h, m)) {
                    r = m;
                }
                else {
                    l = m + 1;
                }
            }
            // 能吃完香蕉的最小速度
            return l;
        }

        private boolean eatAllWithSpeed(int[] piles, int h, int speed) {
            int useHour = 0;
            for (int pile : piles) {
                useHour += ((pile + speed - 1) / speed);
            }
            return useHour <= h;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
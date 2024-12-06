package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


public class GuessNumberHigherOrLowerTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {

    }

    class GuessGame {

        int guess(int num) {
            return -1;
        }

    }

    // leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Forward declaration of guess API.
     *
     * @param num your guess
     * @return -1 if num is lower than the guess number 1 if num is higher than the guess number otherwise return 0 int
     * guess(int num);
     */

    public class Solution extends GuessGame {

        public int guessNumber(int n) {
            int left = 1, right = n;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                final int guess = guess(mid);
                if (guess == 0) {
                    return mid;
                }
                else if (guess == 1) {
                    left = mid + 1;
                }
                else {
                    right = mid - 1;
                }
            }
            // never go here
            return -1;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
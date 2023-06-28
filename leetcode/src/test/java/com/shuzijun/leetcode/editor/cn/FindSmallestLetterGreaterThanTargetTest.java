package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class FindSmallestLetterGreaterThanTargetTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final char[] letters = {'c', 'f', 'g'};
    then(solution.nextGreatestLetter(letters, 'a')).isEqualTo('c');
    then(solution.nextGreatestLetter(letters, 'c')).isEqualTo('f');
    then(solution.nextGreatestLetter(letters, 'd')).isEqualTo('f');
    then(solution.nextGreatestLetter(letters, 'j')).isEqualTo('c');
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public char nextGreatestLetter(char[] letters, char target) {
      if (target < letters[0] || target >= letters[letters.length - 1]) {
        return letters[0];
      }
      int left = 0, right = letters.length - 1;
      while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (letters[mid] > target) {
          right = mid;
        } else {
          left = mid + 1;
        }
      }
      return letters[left];
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
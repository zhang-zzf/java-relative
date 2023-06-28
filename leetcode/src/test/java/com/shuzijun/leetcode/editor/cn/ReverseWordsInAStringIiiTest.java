//给定一个字符串
// s ，你需要反转字符串中每个单词的字符顺序，同时仍保留空格和单词的初始顺序。 
//
// 
//
// 示例 1： 
//
// 
//输入：s = "Let's take LeetCode contest"
//输出："s'teL ekat edoCteeL tsetnoc"
// 
//
// 示例 2: 
//
// 
//输入： s = "God Ding"
//输出："doG gniD"
// 
//
// 
//
// 提示： 
//
// 
// 1 <= s.length <= 5 * 10⁴ 
// 
// s 包含可打印的 ASCII 字符。 
// 
// s 不包含任何开头或结尾空格。 
// 
// s 里 至少 有一个词。 
// 
// s 中的所有单词都用一个空格隔开。 
// 
//
// Related Topics 双指针 字符串 
// 👍 506 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


public class ReverseWordsInAStringIiiTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {

  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public String reverseWords(String s) {
      StringBuilder ans = new StringBuilder();
      int start = -1, end = -1;
      for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        if (c != ' ') {
          if (start == -1) {
            start = i;
          }
          end = i;
        } else {
          if (start != -1) {
            reverseAppend(s, start, end, ans);
            start = end = -1;
          }
          ans.append(c);
        }
      }
      if (start != -1) {
        reverseAppend(s, start, end, ans);
      }
      return ans.toString();
    }

    private void reverseAppend(String s, int start, int end, StringBuilder ans) {
      for (int j = end; j >= start; j--) {
        ans.append(s.charAt(j));
      }
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
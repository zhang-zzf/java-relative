package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class StringParseTest {


  @Test
  void given_when_then() {
    Solution solution = new Solution();
    char[] parse = solution.parse("2A11B3C");
  }


  @Test
  void givenString_whenDeal_then() {
    Solution solution = new Solution();
    List<String> result = solution.deal(solution.parse("2A1B1C"));
    then(result).hasSize(12);
  }

  class Solution {

    /**
     * 对一字符串，如：2A2B2C，解析字符串所包含的字母，并找出所有的排列组合。 备注：2A表示为2个A字母。
     */
    public char[] parse(String str) {
      StringBuilder ret = new StringBuilder();
      char[] chars = str.toCharArray();
      int num = 0;
      for (int i = 0; i < chars.length; i++) {
        char aChar = chars[i];
        if (aChar >= '0' && aChar <= '9') {
          num = num * 10 + (aChar - '0');
        } else {
          for (int j = 0; j < num; j++) {
            ret.append(aChar);
          }
          num = 0;
        }
      }
      return ret.toString().toCharArray();
    }

    public List<String> deal(char[] chars) {
      List<String> result = new ArrayList<>();
      backTrack(chars, new ArrayList<>(), result);
      return result;
    }

    private void backTrack(char[] chars, List<Integer> trace, List<String> result) {
      if (trace.size() == chars.length) {
        // 转结果
        char[] resp = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
          resp[i] = chars[trace.get(i)];
        }
        result.add(new String(resp));
        return;
      }
      Set<Character> set = new HashSet<>(4);
      for (int i = 0; i < chars.length; i++) {
        if (trace.contains(i)) {
          continue;
        }
        if (!set.add(chars[i])) {
          continue;
        }
        // 选择
        trace.add(i);
        backTrack(chars, trace, result);
        // 回滚选择
        trace.remove(trace.size() - 1);
      }
    }

  }

}

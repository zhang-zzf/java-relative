//数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。 
//
// 
//
// 示例 1： 
//
// 
//输入：n = 3
//输出：["((()))","(()())","(())()","()(())","()()()"]
// 
//
// 示例 2： 
//
// 
//输入：n = 1
//输出：["()"]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= n <= 8 
// 
// Related Topics 字符串 动态规划 回溯 👍 2347 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;


public class GenerateParenthesesTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final List<String> strings = solution.generateParenthesis(3);
        then(strings).hasSize(5);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        class Track {

            private final int length;

            public Track(int n) {
                length = n;
            }

            StringBuilder buf = new StringBuilder();
            //（ 的数量
            int _0 = 0;
            // ) 的数量
            int _1 = 0;

            public boolean isFullFilled() {
                return buf.length() == 2 * length;
            }

            @Override
            public String toString() {
                return buf.toString();
            }

            public boolean select(char c) {
                if ('(' == c && _0 < length) {
                    _0 += 1;
                    buf.append(c);
                    return true;
                } else if (')' == c && _1 - _0 < 0) {
                    _1 += 1;
                    buf.append(c);
                    return true;
                }
                return false;
            }

            public void deSelect() {
                final int lastChar = buf.length() - 1;
                final char c = buf.charAt(lastChar);
                if ('(' == c) {
                    _0 -= 1;
                } else {
                    _1 -= 1;
                }
                buf.delete(lastChar, buf.length());
            }

        }

        public List<String> generateParenthesis(int n) {
            List<String> ret = new ArrayList<>();
            backTrack(new Track(n), ret);
            return ret;
        }

        private final char[] candidate = {'(', ')'};

        private void backTrack(Track data, List<String> ret) {
            if (data.isFullFilled()) {
                ret.add(data.toString());
                return;
            }
            for (char c : candidate) {
                if (data.select(c)) {
                    backTrack(data, ret);
                    data.deSelect();
                }
            }
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
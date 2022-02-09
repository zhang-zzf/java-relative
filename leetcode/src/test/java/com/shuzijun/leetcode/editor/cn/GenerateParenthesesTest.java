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

        public List<String> generateParenthesis(int n) {
            List<String> ret = new ArrayList<>();
            backTrack(n, 0, 0, new StringBuilder(), ret);
            return ret;
        }

        private void backTrack(int n, int lc, int rc, StringBuilder buf, List<String> ret) {
            if (lc == n && rc == n) {
                ret.add(buf.toString());
                return;
            }
            if (lc < n) {
                // 选择
                backTrack(n, lc + 1, rc, buf.append('('), ret);
                // 回退选择
                buf.delete(buf.length() - 1, buf.length());
            }
            if (rc < lc) {
                // 选择
                backTrack(n, lc, rc + 1, buf.append(")"), ret);
                // 回退选择
                buf.delete(buf.length() - 1, buf.length());
            }
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
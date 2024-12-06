// 给定一个字符串S，通过将字符串S中的每个字母转变大小写，我们可以获得一个新的字符串。返回所有可能得到的字符串集合。
//
// 
//
// 示例：
// 输入：S = "a1b2"
// 输出：["a1b2", "a1B2", "A1b2", "A1B2"]
//
// 输入：S = "3z4"
// 输出：["3z4", "3Z4"]
//
// 输入：S = "12345"
// 输出：["12345"]
// 
//
// 
//
// 提示： 
//
// 
// S 的长度不超过12。 
// S 仅由数字和字母组成。 
// 
// Related Topics 位运算 字符串 回溯 👍 354 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


public class LetterCasePermutationTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final List<String> list = solution.letterCasePermutation("a1b2");
        then(list).hasSize(4);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public List<String> letterCasePermutation(String s) {
            List<String> ret = new ArrayList<>();
            ret.add(s);
            backTrace(s.toCharArray(), 0, ret);
            return ret;
        }

        private void backTrace(char[] buf, int idx, List<String> ret) {
            for (; idx < buf.length; idx++) {
                final char c = buf[idx];
                if (c >= 'A' && c <= 'z') {
                    // 选择
                    if (c >= 'a') {
                        // 小写转大写
                        buf[idx] = (char) (c - 32);
                    }
                    else {
                        // 大写转小写
                        buf[idx] = (char) (c + 32);
                    }
                    ret.add(new String(buf));
                    backTrace(buf, idx + 1, ret);
                    // 回滚选择
                    buf[idx] = c;
                }
            }
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)
}

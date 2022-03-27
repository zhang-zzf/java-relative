
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.BDDAssertions.then;


class WordBreakIiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final List<String> strings = solution.wordBreak("catsanddog", Arrays.asList("cat", "cats", "and", "sand", "dog"));
        then(strings).isNotEmpty();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public List<String> wordBreak(String s, List<String> wordDict) {
            Set<String> set = new HashSet<>(wordDict);
            List<String> ans = new LinkedList<>();
            StringBuilder trace = new StringBuilder();
            backTrace(s, set, trace, ans, 0);
            return ans;
        }

        private void backTrace(String s, Set<String> set, StringBuilder trace, List<String> ans, int startIdx) {
            if (startIdx == s.length()) {
                ans.add(trace.substring(1));
            }
            for (int i = startIdx; i < Math.min(s.length(), startIdx + 10); i++) {
                String word = s.substring(startIdx, i + 1);
                if (!set.contains(word)) {
                    continue;
                }
                // 选择
                trace.append(' ').append(word);
                backTrace(s, set, trace, ans, i + 1);
                // 回滚选择
                trace.delete(trace.length() - word.length() - 1, trace.length());
            }
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;


class WordBreakTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.wordBreak("leetcode", Arrays.asList("leet", "code"))).isTrue();
        then(solution.wordBreak("applepenapple", Arrays.asList("apple", "pen"))).isTrue();
        then(solution.wordBreak("catsandog",
            Arrays.asList("cats", "dog", "sand", "and", "cat"))).isFalse();
        // failed case 1
        then(solution.wordBreak("aaaaaaa", Arrays.asList("aaaa", "aaa"))).isTrue();
        // failed case 2
        then(solution.wordBreak("goalspecial",
            Arrays.asList("go", "goal", "goals", "special"))).isTrue();
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public boolean wordBreak(String s, List<String> wordDict) {
            final Set<String> set = new HashSet<>(wordDict);
            boolean[] dp = new boolean[s.length() + 1];
            dp[0] = true;
            for (int w = 0; w < s.length(); w++) {
                for (int i = w; i >= Math.max(0, w - 20); i--) {
                    if (dp[i] && set.contains(s.substring(i, w + 1))) {
                        dp[w + 1] = true;
                        break;
                    }
                }
            }
            return dp[s.length()];
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
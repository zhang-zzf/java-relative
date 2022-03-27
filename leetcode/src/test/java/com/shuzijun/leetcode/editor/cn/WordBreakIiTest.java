
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
            final int lng = s.length();
            Map<Integer, List<String>> lngToWorkBreak = new HashMap<>(lng);
            lngToWorkBreak.put(0, Collections.emptyList());
            for (int i = 0; i < lng; i++) {
                // 遍历所有的可能性
                List<String> ans = new ArrayList<>();
                for (int j = i; j >= Math.max(0, i - 10); j--) {
                    final List<String> list = lngToWorkBreak.get(j);
                    final String word;
                    if (list != null && set.contains(word = s.substring(j, i + 1))) {
                        for (String w : list) {
                            ans.add(w + " " + word);
                        }
                        if (list.isEmpty()) {
                            ans.add(word);
                        }
                    }
                }
                if (!ans.isEmpty()) {
                    lngToWorkBreak.put(i + 1, ans);
                }
            }
            return lngToWorkBreak.getOrDefault(lng, Collections.emptyList());
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
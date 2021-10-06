//给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。答案可以按 任意顺序 返回。 
//
// 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。 
//
// 
//
// 
//
// 示例 1： 
//
// 
//输入：digits = "23"
//输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]
// 
//
// 示例 2： 
//
// 
//输入：digits = ""
//输出：[]
// 
//
// 示例 3： 
//
// 
//输入：digits = "2"
//输出：["a","b","c"]
// 
//
// 
//
// 提示： 
//
// 
// 0 <= digits.length <= 4 
// digits[i] 是范围 ['2', '9'] 的一个数字。 
// 
// Related Topics 哈希表 字符串 回溯 👍 1532 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.BDDAssertions.then;


public class LetterCombinationsOfAPhoneNumberTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final List<String> strings = solution.letterCombinations("23");
        then(strings).contains("ad", "ae", "af", "bd", "be", "bf", "cf", "ce", "cd");
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public List<String> letterCombinations(String digits) {
            Map<String, String[]> map = new HashMap<String, String[]>() {{
                put("2", new String[]{"a", "b", "c"});
                put("3", new String[]{"d", "e", "f"});
                put("4", new String[]{"g", "h", "i"});
                put("5", new String[]{"j", "k", "l"});
                put("6", new String[]{"m", "n", "o"});
                put("7", new String[]{"p", "q", "r", "s"});
                put("8", new String[]{"t", "u", "v"});
                put("9", new String[]{"w", "x", "y", "z"});
            }};
            List<String> ret = new ArrayList<>();
            for (int i = 0; i < digits.length(); i++) {
                final String[] strings = map.get(digits.substring(i, i + 1));
                if (ret.isEmpty()) {
                    ret.addAll(Arrays.asList(strings));
                } else {
                    List<String> newList = new ArrayList<>(ret.size() * strings.length);
                    for (String cur : ret) {
                        for (String suffix : strings) {
                            newList.add(cur + suffix);
                        }
                    }
                    ret = newList;
                }
            }
            return ret;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
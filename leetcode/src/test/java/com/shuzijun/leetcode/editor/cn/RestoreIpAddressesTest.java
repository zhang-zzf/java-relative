//有效 IP 地址 正好由四个整数（每个整数位于 0 到 255 之间组成，且不能含有前导 0），整数之间用 '.' 分隔。 
//
// 
// 例如："0.1.2.201" 和 "192.168.1.1" 是 有效 IP 地址，但是 "0.011.255.245"、"192.168.1.312" 
//和 "192.168@1.1" 是 无效 IP 地址。 
// 
//
// 给定一个只包含数字的字符串 s ，用以表示一个 IP 地址，返回所有可能的有效 IP 地址，这些地址可以通过在 s 中插入 '.' 来形成。你不能重新排序
//或删除 s 中的任何数字。你可以按 任何 顺序返回答案。 
//
// 
//
// 示例 1： 
//
// 
//输入：s = "25525511135"
//输出：["255.255.11.135","255.255.111.35"]
// 
//
// 示例 2： 
//
// 
//输入：s = "0000"
//输出：["0.0.0.0"]
// 
//
// 示例 3： 
//
// 
//输入：s = "1111"
//输出：["1.1.1.1"]
// 
//
// 示例 4： 
//
// 
//输入：s = "010010"
//输出：["0.10.0.10","0.100.1.0"]
// 
//
// 示例 5： 
//
// 
//输入：s = "101023"
//输出：["1.0.10.23","1.0.102.3","10.1.0.23","10.10.2.3","101.0.2.3"]
// 
//
// 
//
// 提示： 
//
// 
// 0 <= s.length <= 20 
// s 仅由数字组成 
// 
// Related Topics 字符串 回溯 👍 767 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.BDDAssertions.then;


public class RestoreIpAddressesTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final List<String> list = solution.restoreIpAddresses("1111");
        then(list).hasSize(1);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        /**
         * 解题思路：https://www.yuque.com/u1147067/vzaha9/ybo76y#EWutL
         */
        public List<String> restoreIpAddresses(String s) {
            List<String> ret = new ArrayList<>();
            List<String> track = new ArrayList<>(4);
            backTrack(s, 0, track, ret);
            return ret;
        }

        private void backTrack(String str, int strIdx, List<String> track, List<String> ret) {
            final int curSubIpIdx = track.size();
            if (curSubIpIdx == 4 && strIdx == str.length()) {
                // 添加到结果集
                ret.add(String.join(".", track.stream().map(String::valueOf).collect(Collectors.toList())));
                return;
            }
            int leftStrLength = str.length() - strIdx;
            int min = Math.max(leftStrLength - (4 - curSubIpIdx - 1) * 3, 1);
            int max = Math.min(leftStrLength - (4 - curSubIpIdx - 1) * 1, 3);
            for (int i = min; i <= max; i++) {
                String subStr = str.substring(strIdx, strIdx + i);
                if (!isSubIp(subStr)) {
                    continue;
                }
                // 选择
                track.add(subStr);
                backTrack(str, strIdx + i, track, ret);
                track.remove(track.size() - 1);
            }
        }

        private boolean isSubIp(String subStr) {
            if ("0".equals(subStr)) {
                return true;
            }
            if (subStr.startsWith("0")) {
                return false;
            }
            final Integer ip = Integer.valueOf(subStr);
            if (ip > 0 && ip <= 255) {
                return true;
            }
            return false;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
package com.github.learn.leetcode.domain.service.common;

import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/21
 */
@Service
public class StringReverse {
    /**
     * 编写一个函数，其作用是将输入的字符串反转过来。输入字符串以字符数组 char[] 的形式给出。
     * <p>
     * 不要给另外的数组分配额外的空间，你必须原地修改输入数组、使用 O(1) 的额外空间解决这一问题。
     * <p>
     * 你可以假设数组中的所有字符都是 ASCII 码表中的可打印字符。
     * <p>
     *  
     * <p>
     * 示例 1：
     * <p>
     * 输入：["h","e","l","l","o"]
     * <p>
     * 输出：["o","l","l","e","h"]
     * <p>
     * 示例 2：
     * <p>
     * 输入：["H","a","n","n","a","h"]
     * <p>
     * 输出：["h","a","n","n","a","H"]
     * <p>
     * 作者：力扣 (LeetCode)
     * 链接：https://leetcode-cn.com/leetbook/read/top-interview-questions-easy/xnhbqj/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param s
     */
    public void reverse(@NotNull char[] s) {
        for (int i = 0, j = s.length - 1; i < j; i++, j--) {
            char tmp = s[i];
            s[i] = s[j];
            s[j] = tmp;
        }
    }
}

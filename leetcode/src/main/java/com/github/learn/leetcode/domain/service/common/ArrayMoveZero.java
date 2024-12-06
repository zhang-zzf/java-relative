package com.github.learn.leetcode.domain.service.common;

import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/21
 */
@Service
public class ArrayMoveZero {


    /**
     * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
     * <p>
     * 示例:
     * <p>
     * 输入: [0,1,0,3,12]
     * <p>
     * 输出: [1,3,12,0,0]
     * <p>
     * 说明:
     * <p>
     * 必须在原数组上操作，不能拷贝额外的数组。
     * <p>
     * 尽量减少操作次数。
     * <p>
     * <p>
     * 作者：力扣 (LeetCode) 链接：https://leetcode-cn.com/leetbook/read/top-interview-questions-easy/x2ba4i/ 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param array
     */
    public void move(int[] array) {
        int j = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) {
                array[++j] = array[i];
            }
        }
        for (int i = j + 1; i < array.length; i++) {
            array[i] = 0;
        }
    }
}

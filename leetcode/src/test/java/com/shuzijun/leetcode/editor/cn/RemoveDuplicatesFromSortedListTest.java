// 给定一个已排序的链表的头 head ， 删除所有重复的元素，使每个元素只出现一次 。返回 已排序的链表 。
//
// 
//
// 示例 1： 
//
// 
// 输入：head = [1,1,2]
// 输出：[1,2]
// 
//
// 示例 2： 
//
// 
// 输入：head = [1,1,2,3,3]
// 输出：[1,2,3]
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点数目在范围 [0, 300] 内 
// -100 <= Node.val <= 100 
// 题目数据保证链表已经按升序 排列 
// 
// Related Topics 链表 👍 726 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


public class RemoveDuplicatesFromSortedListTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {

    }

    // leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for singly-linked list. public class ListNode { int val; ListNode next; ListNode() {} ListNode(int
     * val) { this.val = val; } ListNode(int val, ListNode next) { this.val = val; this.next = next; } }
     */
    class Solution {

        public ListNode deleteDuplicates(ListNode head) {
            ListNode ptr = head;
            while (ptr != null && ptr.next != null) {
                if (ptr.next.val == ptr.val) {
                    ptr.next = ptr.next.next;
                }
                else {
                    ptr = ptr.next;
                }
            }
            return head;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
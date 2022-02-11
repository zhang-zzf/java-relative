//给定一个单链表 L 的头节点 head ，单链表 L 表示为： 
//
// 
//L0 → L1 → … → Ln - 1 → Ln
// 
//
// 请将其重新排列后变为： 
//
// 
//L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → … 
//
// 不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。 
//
// 
//
// 示例 1： 
//
// 
//
// 
//输入：head = [1,2,3,4]
//输出：[1,4,2,3] 
//
// 示例 2： 
//
// 
//
// 
//输入：head = [1,2,3,4,5]
//输出：[1,5,2,4,3] 
//
// 
//
// 提示： 
//
// 
// 链表的长度范围为 [1, 5 * 10⁴] 
// 1 <= node.val <= 1000 
// 
// Related Topics 栈 递归 链表 双指针 👍 788 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


public class ReorderListTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final ListNode<Integer> dummy = new ListNode<>();
        dummy.addToTail(new ListNode<>(1))
                .addToTail(new ListNode<>(2))
                .addToTail(new ListNode<>(3))
                .addToTail(new ListNode<>(4))
                .addToTail(new ListNode<>(5))
                .addToTail(new ListNode<>(6))
        ;
        solution.reorderList(dummy.next);
    }

    //leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     * int val;
     * ListNode next;
     * ListNode() {}
     * ListNode(int val) { this.val = val; }
     * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     * }
     */
    class Solution {

        public void reorderList(ListNode head) {
            ListNode fast = head, slow = head;
            while (fast != null && fast.next != null) {
                fast = fast.next.next;
                slow = slow.next;
            }
            // slow is the middle node of the list
            ListNode rightHalf = slow.next;
            // slow must be the new end of the list
            slow.next = null;
            // reverse
            rightHalf = reverseList(rightHalf);
            ListNode originPtr = head;
            for (ListNode ptr = rightHalf; ptr != null; ) {
                ListNode ptrN = ptr.next;
                ptr.next = originPtr.next;
                originPtr.next = ptr;
                originPtr = ptr.next;
                ptr = ptrN;
            }
        }

        private ListNode reverseList(ListNode head) {
            ListNode cur = head, pre = null;
            while (cur != null) {
                ListNode next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            return pre;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
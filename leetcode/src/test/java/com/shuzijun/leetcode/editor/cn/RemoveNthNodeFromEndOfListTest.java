// 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
//
// 
//
// 示例 1： 
// 
// 
// 输入：head = [1,2,3,4,5], n = 2
// 输出：[1,2,3,5]
// 
//
// 示例 2： 
//
// 
// 输入：head = [1], n = 1
// 输出：[]
// 
//
// 示例 3： 
//
// 
// 输入：head = [1,2], n = 1
// 输出：[1]
// 
//
// 
//
// 提示： 
//
// 
// 链表中结点的数目为 sz 
// 1 <= sz <= 30 
// 0 <= Node.val <= 100 
// 1 <= n <= sz 
// 
//
// 
//
// 进阶：你能尝试使用一趟扫描实现吗？ 
//
// Related Topics 链表 双指针 
// 👍 2345 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class RemoveNthNodeFromEndOfListTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        ListNode<Integer> head = new ListNode<>(1, new ListNode(2, new ListNode(3, new ListNode(4))));
        ListNode removed = solution.removeNthFromEnd(head, 2);
        then(ListNode.toString(removed)).isEqualTo("[1,2,4]");
    }

    // leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for singly-linked list. public class ListNode { int val; ListNode next; ListNode() {} ListNode(int
     * val) { this.val = val; } ListNode(int val, ListNode next) { this.val = val; this.next = next; } }
     */
    class Solution {

        public ListNode removeNthFromEnd(ListNode head, int n) {
            ListNode dummy = new ListNode(0, head);
            ListNode f = dummy, s = dummy;
            // move the fast pointer
            while (n-- > 0) {
                f = f.next;
            }
            while (f.next != null) {
                f = f.next;
                s = s.next;
            }
            // remove the Nth iterm
            s.next = s.next.next;
            return dummy.next;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
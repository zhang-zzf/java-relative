// 定义一个函数，输入一个链表的头节点，反转该链表并输出反转后链表的头节点。
//
// 
//
// 示例: 
//
// 输入: 1->2->3->4->5->NULL
// 输出: 5->4->3->2->1->NULL
//
// 
//
// 限制： 
//
// 0 <= 节点个数 <= 5000 
//
// 
//
// 注意：本题与主站 206 题相同：https://leetcode-cn.com/problems/reverse-linked-list/ 
// Related Topics 递归 链表 👍 362 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class FanZhuanLianBiaoLcofTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final ListNode n3 = new ListNode(3);
        final ListNode n2 = new ListNode(2);
        final ListNode n1 = new ListNode(1);
        n1.next = n2;
        n2.next = n3;
        final ListNode listNode = solution.reverseList(n1);
        then(listNode.val).isEqualTo(3);
    }

    public class ListNode {

        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }

    }
    // leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for singly-linked list. public class ListNode { int val; ListNode next; ListNode(int x) { val = x; }
     * }
     */
    class Solution {

        public ListNode reverseList(ListNode head) {
            if (head == null) {
                return null;
            }
            if (head.next != null) {
                final ListNode subListHead = reverseList(head.next);
                head.next.next = head;
                head.next = null;
                return subListHead;
            }
            else {
                return head;
            }
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
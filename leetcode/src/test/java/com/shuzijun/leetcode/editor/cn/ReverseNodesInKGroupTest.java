//给你一个链表，每 k 个节点一组进行翻转，请你返回翻转后的链表。 
//
// k 是一个正整数，它的值小于或等于链表的长度。 
//
// 如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。 
//
// 进阶： 
//
// 
// 你可以设计一个只使用常数额外空间的算法来解决此问题吗？ 
// 你不能只是单纯的改变节点内部的值，而是需要实际进行节点交换。 
// 
//
// 
//
// 示例 1： 
//
// 
//输入：head = [1,2,3,4,5], k = 2
//输出：[2,1,4,3,5]
// 
//
// 示例 2： 
//
// 
//输入：head = [1,2,3,4,5], k = 3
//输出：[3,2,1,4,5]
// 
//
// 示例 3： 
//
// 
//输入：head = [1,2,3,4,5], k = 1
//输出：[1,2,3,4,5]
// 
//
// 示例 4： 
//
// 
//输入：head = [1], k = 1
//输出：[1]
// 
//
// 
// 
//
// 提示： 
//
// 
// 列表中节点的数量在范围 sz 内 
// 1 <= sz <= 5000 
// 0 <= Node.val <= 1000 
// 1 <= k <= sz 
// 
// Related Topics 递归 链表 👍 1463 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class ReverseNodesInKGroupTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final ListNode<Integer> dummy = new ListNode<>();
        dummy.addToTail(new ListNode<>(1))
                .addToTail(new ListNode<>(2))
                .addToTail(new ListNode<>(3))
                .addToTail(new ListNode<>(4))
                .addToTail(new ListNode<>(5))
        ;
        final ListNode reverseKGroup = solution.reverseKGroup(dummy.next, 2);
        then(reverseKGroup.val).isEqualTo(2);
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

        public ListNode reverseKGroup(ListNode head, int k) {
            final ListNode dummy = new ListNode(-1, head);
            ListNode start = dummy;
            while (true) {
                ListNode end = start;
                for (int i = 0; i < k; i++) {
                    if (end != null) {
                        end = end.next;
                    } else {
                        break;
                    }
                }
                // 剩余的 node 数量 < k
                if (end == null) {
                    break;
                }
                ListNode startNext = start.next, endNext = end.next;
                // 调整指针
                start.next = end;
                // reverseList
                reverseList(startNext, end);
                startNext.next = endNext;
                // 开启下一次循环
                start = startNext;
            }
            return dummy.next;
        }

        private void reverseList(ListNode start, ListNode end) {
            ListNode pre = start, cur = pre.next;
            while (pre != end && cur != null) {
                ListNode next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
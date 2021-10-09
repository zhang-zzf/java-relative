//给定一个链表，两两交换其中相邻的节点，并返回交换后的链表。
//
// 你不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。
//
//
//
// 示例 1：
//
//
//输入：head = [1,2,3,4]
//输出：[2,1,4,3]
//
//
// 示例 2：
//
//
//输入：head = []
//输出：[]
//
//
// 示例 3：
//
//
//输入：head = [1]
//输出：[1]
//
//
//
//
// 提示：
//
//
// 链表中节点的数目在范围 [0, 100] 内
// 0 <= Node.val <= 100
//
//
//
//
// 进阶：你能在不修改链表节点值的情况下解决这个问题吗?（也就是说，仅修改节点本身。）
// Related Topics 递归 链表 👍 1070 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class SwapNodesInPairsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.swapPairs(new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4))))).val)
            .isEqualTo(2);
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

        public ListNode swapPairs(ListNode head) {
            final ListNode dummy = new ListNode(0, head);
            for (ListNode tmp = dummy, node1, node2;
                 tmp != null && (node1 = tmp.next) != null && (node2 = node1.next) != null; tmp = node1) {
                tmp.next = node2;
                node1.next = node2.next;
                node2.next = node1;
            }
            return dummy.next;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
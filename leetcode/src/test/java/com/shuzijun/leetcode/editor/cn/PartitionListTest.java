//给你一个链表的头节点 head 和一个特定值 x ，请你对链表进行分隔，使得所有 小于 x 的节点都出现在 大于或等于 x 的节点之前。 
//
// 你应当 保留 两个分区中每个节点的初始相对位置。 
//
// 
//
// 示例 1： 
//
// 
//输入：head = [1,4,3,2,5,2], x = 3
//输出：[1,2,2,4,3,5]
// 
//
// 示例 2： 
//
// 
//输入：head = [2,1], x = 2
//输出：[1,2]
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点的数目在范围 [0, 200] 内 
// -100 <= Node.val <= 100 
// -200 <= x <= 200 
// 
// Related Topics 链表 双指针 👍 524 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


public class PartitionListTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {

    }

    public class ListNode {

        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

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

        public ListNode partition(ListNode head, int x) {
            final ListNode dummy = new ListNode(0, head);
            ListNode slow = dummy, fast = head;
            while (fast != null) {
                if (fast.val < x) {
                    slow = fast;
                    fast = fast.next;
                } else {
                    // fast.val >= x
                    // find node that node.val < x
                    ListNode nodeGtX = fast;
                    while (fast != null) {
                        if (fast.val < x) {
                            break;
                        }
                        nodeGtX = fast;
                        fast = fast.next;
                    }
                    if (fast == null) {
                        // reach end, exit all loop
                        break;
                    }
                    // adjust point
                    ListNode slowNext = slow.next;
                    slow.next = fast;
                    nodeGtX.next = fast.next;
                    fast.next = slowNext;
                    slow = fast;
                    fast = nodeGtX;
                }
            }
            return dummy.next;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
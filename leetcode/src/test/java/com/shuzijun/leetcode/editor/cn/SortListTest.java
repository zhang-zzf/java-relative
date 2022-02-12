//给你链表的头结点 head ，请将其按 升序 排列并返回 排序后的链表 。 
//
// 
// 
//
// 
//
// 示例 1： 
//
// 
//输入：head = [4,2,1,3]
//输出：[1,2,3,4]
// 
//
// 示例 2： 
//
// 
//输入：head = [-1,5,3,4,0]
//输出：[-1,0,3,4,5]
// 
//
// 示例 3： 
//
// 
//输入：head = []
//输出：[]
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点的数目在范围 [0, 5 * 10⁴] 内 
// -10⁵ <= Node.val <= 10⁵ 
// 
//
// 
//
// 进阶：你可以在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序吗？ 
// Related Topics 链表 双指针 分治 排序 归并排序 👍 1457 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class SortListTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final ListNode head = new ListNode(-1, new ListNode(5, new ListNode(3, new ListNode(4, new ListNode(0)))));
        final ListNode sortL = solution.sortList(head);
        then(sortL.val).isEqualTo(-1);
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

        public ListNode sortList(ListNode head) {
            if (head == null) {
                return null;
            }
            // length of the list
            int lng = 0;
            for (ListNode ptr = head; ptr != null; ptr = ptr.next) {
                lng += 1;
            }
            final ListNode dummy = new ListNode(0, head);
            for (int subLng = 1; subLng < lng; subLng <<= 1) {
                ListNode pre = dummy, cur = dummy.next;
                while (cur != null) {
                    // 第1段链表
                    ListNode head1 = cur;
                    for (int i = 1; i < subLng && cur.next != null; i++) {
                        cur = cur.next;
                    }
                    ListNode head2 = cur.next;
                    cur.next = null;
                    // 第1段链表
                    // 第2段链表
                    cur = head2;
                    for (int i = 1; i < subLng && cur != null && cur.next != null; i++) {
                        cur = cur.next;
                    }
                    ListNode next = null;
                    if (cur != null) {
                        next = cur.next;
                        cur.next = null;
                    }
                    // 第2段链表
                    // 排序
                    pre.next = mergeSort(head1, head2);
                    // 链接
                    while (pre.next != null) {
                        pre = pre.next;
                    }
                    cur = next;
                }

            }
            return dummy.next;
        }

        private ListNode mergeSort(ListNode ptr1, ListNode ptr2) {
            final ListNode dummy = new ListNode();
            ListNode tail = dummy;
            while (ptr1 != null && ptr2 != null) {
                if (ptr1.val < ptr2.val) {
                    tail.next = ptr1;
                    ptr1 = ptr1.next;
                } else {
                    tail.next = ptr2;
                    ptr2 = ptr2.next;
                }
                tail = tail.next;
            }
            if (ptr1 != null) {
                tail.next = ptr1;
            }
            if (ptr2 != null) {
                tail.next = ptr2;
            }
            return dummy.next;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
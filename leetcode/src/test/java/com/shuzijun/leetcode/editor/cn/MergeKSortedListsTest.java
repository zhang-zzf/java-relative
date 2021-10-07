//给你一个链表数组，每个链表都已经按升序排列。 
//
// 请你将所有链表合并到一个升序链表中，返回合并后的链表。 
//
// 
//
// 示例 1： 
//
// 输入：lists = [[1,4,5],[1,3,4],[2,6]]
//输出：[1,1,2,3,4,4,5,6]
//解释：链表数组如下：
//[
//  1->4->5,
//  1->3->4,
//  2->6
//]
//将它们合并到一个有序链表中得到。
//1->1->2->3->4->4->5->6
// 
//
// 示例 2： 
//
// 输入：lists = []
//输出：[]
// 
//
// 示例 3： 
//
// 输入：lists = [[]]
//输出：[]
// 
//
// 
//
// 提示： 
//
// 
// k == lists.length 
// 0 <= k <= 10^4 
// 0 <= lists[i].length <= 500 
// -10^4 <= lists[i][j] <= 10^4 
// lists[i] 按 升序 排列 
// lists[i].length 的总和不超过 10^4 
// 
// Related Topics 链表 分治 堆（优先队列） 归并排序 👍 1547 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class MergeKSortedListsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final ListNode[] listNodes = {
            new ListNode(1, new ListNode(4, new ListNode(5))),
            new ListNode(1, new ListNode(3, new ListNode(4))),
            new ListNode(2, new ListNode(6))
        };
        final ListNode head = solution.mergeKLists(listNodes);
        then(head).isNotNull();
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

        public ListNode mergeKLists(ListNode[] lists) {
            final int length = lists.length;
            ListNode[] curNodeArray = new ListNode[length];
            int heapSize = 0;
            // init
            for (int i = 0, j = 0; i < length; i++) {
                if (lists[i] != null) {
                    heapSize += 1;
                    curNodeArray[j++] = lists[i];
                }
            }
            if (heapSize == 0) {
                return null;
            }
            // build min heap
            for (int i = ((length - 1) - 1) / 2; i >= 0; i--) {
                siftDown(curNodeArray, i, heapSize);
            }
            if (heapSize <= 0) {
                return null;
            }
            ListNode dummy = new ListNode();
            ListNode tail = dummy;
            while (curNodeArray[0] != null) {
                tail.next = curNodeArray[0];
                tail = tail.next;
                // move forward
                curNodeArray[0] = curNodeArray[0].next;
                // move heap last item to top
                while (curNodeArray[0] == null && heapSize > 0) {
                    curNodeArray[0] = curNodeArray[--heapSize];
                }
                // rebuild heap
                siftDown(curNodeArray, 0, heapSize);
            }
            return dummy.next;
        }

        private void siftDown(ListNode[] curNodeArray, int curNode, int heapSize) {
            while (true) {
                int minIdx = curNode;
                final int leftChild = (curNode + 1) * 2 - 1;
                if (leftChild < heapSize && curNodeArray[leftChild].val < curNodeArray[minIdx].val) {
                    minIdx = leftChild;
                }
                final int rightChildIdx = leftChild + 1;
                if (rightChildIdx < heapSize && curNodeArray[rightChildIdx].val < curNodeArray[minIdx].val) {
                    minIdx = rightChildIdx;
                }
                swap(curNodeArray, curNode, minIdx);
                if (minIdx == curNode) {
                    break;
                }
                curNode = minIdx;
            }
        }

        private void swap(ListNode[] array, int i1, int i2) {
            ListNode tmp = array[i1];
            array[i1] = array[i2];
            array[i2] = tmp;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
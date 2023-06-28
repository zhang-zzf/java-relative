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

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Comparator;
import java.util.PriorityQueue;
import org.junit.jupiter.api.Test;


public class SortListTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final ListNode head = new ListNode(-1,
        new ListNode(5, new ListNode(3, new ListNode(4, new ListNode(0)))));
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
   * Definition for singly-linked list. public class ListNode { int val; ListNode next; ListNode()
   * {} ListNode(int val) { this.val = val; } ListNode(int val, ListNode next) { this.val = val;
   * this.next = next; } }
   */
  class Solution {

    public ListNode sortList(ListNode head) {
      ListNode dummy = new ListNode(), tail = dummy;
      PriorityQueue<ListNode> pq = new PriorityQueue<>(
          Comparator.comparing(listNode -> listNode.val));
      for (ListNode ptr = head; ptr != null; ptr = ptr.next) {
        pq.add(ptr);
      }
      while (!pq.isEmpty()) {
        tail.next = pq.poll();
        tail = tail.next;
      }
      tail.next = null;
      return dummy.next;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
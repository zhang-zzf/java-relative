//给你单链表的头指针 head 和两个整数 left 和 right ，其中 left <= right 。请你反转从位置 left 到位置 right 的链
//表节点，返回 反转后的链表 。
// 
//
// 示例 1： 
//
// 
//输入：head = [1,2,3,4,5], left = 2, right = 4
//输出：[1,4,3,2,5]
// 
//
// 示例 2： 
//
// 
//输入：head = [5], left = 1, right = 1
//输出：[5]
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点数目为 n 
// 1 <= n <= 500 
// -500 <= Node.val <= 500 
// 1 <= left <= right <= n 
// 
//
// 
//
// 进阶： 你可以使用一趟扫描完成反转吗？ 
// Related Topics 链表 👍 1152 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class ReverseLinkedListIiTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final ListNode<Integer> dummy = new ListNode<>(null);
    dummy.addToTail(new ListNode<>(1))
        .addToTail(new ListNode<>(2))
        .addToTail(new ListNode<>(3))
        .addToTail(new ListNode<>(4))
        .addToTail(new ListNode<>(5));
    final ListNode listNode = solution.reverseBetween(dummy.next, 2, 4);
    then(listNode.next.val).isEqualTo(4);
  }

  //leetcode submit region begin(Prohibit modification and deletion)

  /**
   * Definition for singly-linked list. public class ListNode { int val; ListNode next; ListNode()
   * {} ListNode(int val) { this.val = val; } ListNode(int val, ListNode next) { this.val = val;
   * this.next = next; } }
   */
  class Solution {

    public ListNode reverseBetween(ListNode head, int left, int right) {
      if (left == right) {
        return head;
      }
      final ListNode dummy = new ListNode(-1, head);
      ListNode pre = dummy, cur = head;
      int i = 1;
      for (; i < left; i++) {
        pre = cur;
        cur = cur.next;
      }
      ListNode leftPtrPre = pre;
      for (; i <= right; i++) {
        ListNode next = cur.next;
        cur.next = pre;
        pre = cur;
        cur = next;
      }
      leftPtrPre.next.next = cur;
      leftPtrPre.next = pre;
      return dummy.next;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
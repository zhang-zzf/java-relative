//给你一个链表的头节点 head ，旋转链表，将链表每个节点向右移动 k 个位置。 
//
// 
//
// 示例 1： 
//
// 
//输入：head = [1,2,3,4,5], k = 2
//输出：[4,5,1,2,3]
// 
//
// 示例 2： 
//
// 
//输入：head = [0,1,2], k = 4
//输出：[2,0,1]
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点的数目在范围 [0, 500] 内 
// -100 <= Node.val <= 100 
// 0 <= k <= 2 * 10⁹ 
// 
// Related Topics 链表 双指针 👍 713 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


public class RotateListTest {

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
   * Definition for singly-linked list. public class ListNode { int val; ListNode next; ListNode()
   * {} ListNode(int val) { this.val = val; } ListNode(int val, ListNode next) { this.val = val;
   * this.next = next; } }
   */
  class Solution {

    public ListNode rotateRight(ListNode head, int k) {
      int length = 0;
      for (ListNode ptr = head; ptr != null; ptr = ptr.next) {
        length += 1;
      }
      if (length == 0) {
        return head;
      }
      // 防止重复迁移
      k = k % length;
      ListNode ptrK = head;
      for (int i = 0; i < k; i++) {
        ptrK = ptrK.next;
      }
      ListNode ptr = head;
      for (; ptrK.next != null; ptrK = ptrK.next) {
        ptr = ptr.next;
      }
      // 迁移链表
      ptrK.next = head;
      head = ptr.next;
      ptr.next = null;
      return head;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
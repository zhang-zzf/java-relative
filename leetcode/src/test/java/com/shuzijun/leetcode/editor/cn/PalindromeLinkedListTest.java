//给你一个单链表的头节点 head ，请你判断该链表是否为回文链表。如果是，返回 true ；否则，返回 false 。 
//
// 
//
// 示例 1： 
//
// 
//输入：head = [1,2,2,1]
//输出：true
// 
//
// 示例 2： 
//
// 
//输入：head = [1,2]
//输出：false
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点数目在范围[1, 10⁵] 内 
// 0 <= Node.val <= 9 
// 
//
// 
//
// 进阶：你能否用 O(n) 时间复杂度和 O(1) 空间复杂度解决此题？ 
// Related Topics 栈 递归 链表 双指针 👍 1253 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class PalindromeLinkedListTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final ListNode head = new ListNode(1, new ListNode(2, new ListNode(2, new ListNode(1))));
    final boolean result = solution.isPalindrome(head);
    then(result).isTrue();
  }

  //leetcode submit region begin(Prohibit modification and deletion)

  /**
   * Definition for singly-linked list. public class ListNode { int val; ListNode next; ListNode()
   * {} ListNode(int val) { this.val = val; } ListNode(int val, ListNode next) { this.val = val;
   * this.next = next; } }
   */
  class Solution {

    public boolean isPalindrome(ListNode head) {
      if (head == null) {
        return true;
      }
      ListNode fast = head, slow = head;
      while (fast.next != null && fast.next.next != null) {
        fast = fast.next.next;
        slow = slow.next;
      }
      boolean result = true;
      ListNode reverseHead = reverse(slow.next);
      for (ListNode ptr1 = reverseHead, ptr2 = head;
          ptr1 != null;
          ptr1 = ptr1.next, ptr2 = ptr2.next) {
        if (ptr1.val != ptr2.val) {
          result = false;
          break;
        }
      }
      // 恢复链表
      reverse(reverseHead);
      return result;
    }

    private ListNode reverse(ListNode head) {
      ListNode cur = head, pre = null;
      while (cur != null) {
        ListNode next = cur.next;
        cur.next = pre;
        pre = cur;
        cur = next;
      }
      return pre;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
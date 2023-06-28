//给定一个链表，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。 
//
// 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。 为了表示给定链表中的环，评测系统内部使用整数 pos 来表示链表尾连接到
//链表中的位置（索引从 0 开始）。如果 pos 是 -1，则在该链表中没有环。注意：pos 不作为参数进行传递，仅仅是为了标识链表的实际情况。 
//
// 不允许修改 链表。 
//
// 
// 
//
// 
//
// 示例 1： 
//
// 
//
// 
//输入：head = [3,2,0,-4], pos = 1
//输出：返回索引为 1 的链表节点
//解释：链表中有一个环，其尾部连接到第二个节点。
// 
//
// 示例 2： 
//
// 
//
// 
//输入：head = [1,2], pos = 0
//输出：返回索引为 0 的链表节点
//解释：链表中有一个环，其尾部连接到第一个节点。
// 
//
// 示例 3： 
//
// 
//
// 
//输入：head = [1], pos = -1
//输出：返回 null
//解释：链表中没有环。
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点的数目范围在范围 [0, 10⁴] 内 
// -10⁵ <= Node.val <= 10⁵ 
// pos 的值为 -1 或者链表中的一个有效索引 
// 
//
// 
//
// 进阶：你是否可以使用 O(1) 空间解决此题？ 
// Related Topics 哈希表 链表 双指针 👍 1348 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;


public class LinkedListCycleIiTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final int[] same = solution.same(new int[]{1, 2, 5}, new int[]{2, 5, 5, 9});
    then(same).contains(2, 5);
  }

  @Test
  void givenLinkedList_whenDetectCycle_then() {
    ListNode last = new ListNode(-4);
    ListNode start = new ListNode(2, new ListNode(0, last));
    last.next = start;
    ListNode head = new ListNode(3, start);
    ListNode listNode = new Solution().detectCycle(head);
    then(listNode).isEqualTo(start);
  }

  class ListNode {

    int val;
    ListNode next;

    ListNode(int x) {
      val = x;
      next = null;
    }

    ListNode(int x, ListNode next) {
      val = x;
      this.next = next;
    }


  }

  //leetcode submit region begin(Prohibit modification and deletion)

  /**
   * Definition for singly-linked list. class ListNode { int val; ListNode next; ListNode(int x) {
   * val = x; next = null; } }
   */
  public class Solution {

    public int[] same(int[] nums1, int[] nums2) {
      final Set<Integer> nums1Set = Arrays.stream(nums1).mapToObj(Integer::new)
          .collect(Collectors.toSet());
      List<Integer> ret = new LinkedList<Integer>();
      for (int num : nums2) {
        if (nums1Set.contains(num)) {
          ret.add(num);
        }
      }
      return ret.stream().mapToInt(Integer::intValue).toArray();
    }

    public ListNode detectCycle(ListNode head) {
      ListNode f = head, s = head;
      boolean cycle = false;
      while (f != null && f.next != null) {
        f = f.next.next;
        s = s.next;
        if (f == s) {
          cycle = true;
          break;
        }
      }
      if (!cycle) {
        return null;
      }
      f = head;
      while (f != s) {
        f = f.next;
        s = s.next;
      }
      return f;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
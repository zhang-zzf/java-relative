package com.shuzijun.leetcode.editor.cn;

/**
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2022/1/28
 */
public class ListNode<T> {

  T val;
  ListNode next;

  ListNode() {
  }

  ListNode(T val) {
    this.val = val;
  }

  ListNode(T val, ListNode next) {
    this.val = val;
    this.next = next;
  }

  public static String toString(ListNode head) {
    StringBuilder buf = new StringBuilder("[");
    while (head != null) {
      buf.append(head.val);
      if (head.next != null) {
        buf.append(',');
      }
      head = head.next;
    }
    buf.append(']');
    return buf.toString();
  }

  ListNode<T> addToTail(ListNode<T> next) {
    this.next = next;
    return next;
  }

}




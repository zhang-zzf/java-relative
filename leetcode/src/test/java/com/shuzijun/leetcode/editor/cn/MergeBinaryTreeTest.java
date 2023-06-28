package com.shuzijun.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class MergeBinaryTreeTest {

  /**
   * 题目：合并二叉树
   * <p>
   * 给定两个二叉树，想象当你将它们中的一个覆盖到另一个上时，两个二叉树的一些节点便会重叠。
   * <p>
   * 你需要将他们合并为一个新的二叉树。
   * <p>
   * 合并的规则是如果两个节点重叠，那么将他们的值相加作为节点合并后的新值，否则不为 NULL 的节点将直接作为新二叉树的节点。
   * <p>
   * <p>
   * 示例：
   * <p>
   * 输入: Tree 1                 Tree 2 1                        2 / \                       / \ 3
   * 2                 1   3 /                           \    \ 5                             4   7
   * 输出: 合并后的树: 3 / \ 4   5 /  \    \ 5   4    7
   */


  @Test
  void given_when_then() {
    Node t1 = new Node(1,
        new Node(3, new Node(5), null),
        new Node(2));
    Node t2 = new Node(2,
        new Node(1, null, new Node(4)),
        new Node(3, null, new Node(7)));
    Solution solution = new Solution();
    log.info("Tree 1 -> {}", solution.middle(t1));
    log.info("Tree 2 -> {}", solution.middle(t2));
    Node merged = solution.merge(t1, t2);
    log.info("merged -> {}", solution.middle(merged));
  }

  public static class Node {

    private int val;
    private Node left;
    private Node right;

    public Node(int val) {
      this.val = val;
    }

    public Node(int val, Node left, Node right) {
      this.val = val;
      this.left = left;
      this.right = right;
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("{");
      sb.append("\"val\":").append(val).append(',');
      return sb.replace(sb.length() - 1, sb.length(), "}").toString();
    }

  }

  class Solution {

    public Node merge(Node tree1, Node tree2) {
      if (tree1 == null) {
        return tree2;
      }
      if (tree2 == null) {
        return tree1;
      }
      Node root = new Node(tree1.val + tree2.val);
      root.left = merge(tree1.left, tree2.left);
      root.right = merge(tree1.right, tree2.right);
      return root;
    }

    public List<Node> middle(Node root) {
      List<Node> ret = new ArrayList<>();
      Queue<Node> stack = new LinkedList<>();
      stack.offer(root);
      while (!stack.isEmpty()) {
        Node n = stack.poll();
        ret.add(n);
        if (n != null) {
          stack.offer(n.left);
          stack.offer(n.right);
        }
      }
      int nodeSize = ret.size();
      for (; nodeSize > 0; nodeSize--) {
        if (ret.get(nodeSize - 1) != null) {
          break;
        }
      }
      return ret.subList(0, nodeSize);
    }

  }

}

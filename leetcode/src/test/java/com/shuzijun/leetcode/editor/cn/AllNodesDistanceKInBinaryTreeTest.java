package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.junit.jupiter.api.Test;


public class AllNodesDistanceKInBinaryTreeTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final TreeNode target = new TreeNode(5,
        new TreeNode(6),
        new TreeNode(2,
            new TreeNode(7),
            new TreeNode(4)));
    final TreeNode root = new TreeNode(3,
        target,
        new TreeNode(1,
            new TreeNode(0),
            new TreeNode(8)));
    final List<Integer> ans = solution.distanceK(root, target, 2);
    then(ans).contains(1, 4, 7);
  }

  //leetcode submit region begin(Prohibit modification and deletion)

  /**
   * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode
   * right; TreeNode(int x) { val = x; } }
   */
  class Solution {

    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
      List<Integer> ans = new LinkedList<>();
      // 保存子节点的父节点
      Map<TreeNode, TreeNode> childToParent = new HashMap<>();
      Queue<TreeNode> queue = new LinkedList<>();
      queue.offer(root);
      while (!queue.isEmpty()) {
        final TreeNode poll = queue.poll();
        if (poll.left != null) {
          childToParent.put(poll.left, poll);
          queue.offer(poll.left);
        }
        if (poll.right != null) {
          childToParent.put(poll.right, poll);
          queue.offer(poll.right);
        }
      }
      dfs(target, null, childToParent, 0, k, ans);
      return ans;
    }

    private void dfs(TreeNode root, TreeNode from, Map<TreeNode, TreeNode> childToParent,
        int depth, int k,
        List<Integer> ans) {
      if (root == null) {
        return;
      }
      if (depth == k) {
        ans.add(root.val);
        return;
      }
      // 3个方向 左右父
      if (root.left != from) {
        dfs(root.left, root, childToParent, depth + 1, k, ans);
      }
      if (root.right != from) {
        dfs(root.right, root, childToParent, depth + 1, k, ans);
      }
      TreeNode parent;
      if ((parent = childToParent.get(root)) != from) {
        dfs(parent, root, childToParent, depth + 1, k, ans);
      }
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
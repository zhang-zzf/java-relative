package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class ValidateBinarySearchTreeTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final TreeNode root = new TreeNode(5,
        new TreeNode(1),
        new TreeNode(4,
            new TreeNode(3),
            new TreeNode(6)
        ));
    final boolean validBST = solution.isValidBST(root);
    then(validBST).isFalse();
  }

  //leetcode submit region begin(Prohibit modification and deletion)

  /**
   * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode
   * right; TreeNode() {} TreeNode(int val) { this.val = val; } TreeNode(int val, TreeNode left,
   * TreeNode right) { this.val = val; this.left = left; this.right = right; } }
   */
  class Solution {

    public boolean isValidBST(TreeNode root) {
      return valid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean valid(TreeNode root, long min, long max) {
      if (root == null) {
        return true;
      }
      if (root.val <= min || root.val >= max) {
        return false;
      }
      return valid(root.left, min, root.val) && valid(root.right, root.val, max);
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
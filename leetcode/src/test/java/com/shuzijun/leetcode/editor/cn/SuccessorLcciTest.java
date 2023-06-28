package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class SuccessorLcciTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final TreeNode root = SerializeAndDeserializeBinaryTreeTest.deserialize(
        "[6,4,10,2,5,8,15,1,3,null,null,7,9,11,18]");
    then(solution.inorderSuccessor(root, new TreeNode(12)).val)
        .isEqualTo(15);
    then(solution.inorderPredecessor(root, new TreeNode(12)).val)
        .isEqualTo(11);
  }

  //leetcode submit region begin(Prohibit modification and deletion)

  /**
   * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode
   * right; TreeNode(int x) { val = x; } }
   */
  class Solution {

    public TreeNode inorderPredecessor(TreeNode root, TreeNode p) {
      TreeNode ans = null;
      while (root != null) {
        if (p.val <= root.val) {
          root = root.left;
        } else {
          ans = root;
          root = root.right;
        }
      }
      return ans;
    }

    public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
      TreeNode ans = null;
      while (root != null) {
        if (p.val >= root.val) {
          root = root.right;
        } else {
          ans = root;
          root = root.left;
        }
      }
      return ans;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class BinaryTreePruningTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    String tree = "[1,0,1,0,0,0,1]";
    final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
    final TreeNode root = codec.deserialize(tree);
    final TreeNode pruneTree = solution.pruneTree(root);
    final String serialize = codec.serialize(pruneTree);
    then(serialize).isEqualTo("[1,null,1,null,1]");
  }

  //leetcode submit region begin(Prohibit modification and deletion)

  /**
   * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode
   * right; TreeNode() {} TreeNode(int val) { this.val = val; } TreeNode(int val, TreeNode left,
   * TreeNode right) { this.val = val; this.left = left; this.right = right; } }
   */
  class Solution {

    public TreeNode pruneTree(TreeNode root) {
      if (root == null) {
        return null;
      }
      root.left = pruneTree(root.left);
      root.right = pruneTree(root.right);
      if (root.left == null && root.right == null && root.val == 0) {
        return null;
      }
      return root;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
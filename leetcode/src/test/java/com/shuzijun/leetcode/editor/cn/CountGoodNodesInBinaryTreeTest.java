package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class CountGoodNodesInBinaryTreeTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    String tree = "[3,1,4,3,null,1,5]";
    final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
    final TreeNode root = codec.deserialize(tree);
    final int goodNodes = solution.goodNodes(root);
    then(goodNodes).isEqualTo(4);
  }

  //leetcode submit region begin(Prohibit modification and deletion)

  /**
   * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode
   * right; TreeNode() {} TreeNode(int val) { this.val = val; } TreeNode(int val, TreeNode left,
   * TreeNode right) { this.val = val; this.left = left; this.right = right; } }
   */
  class Solution {

    public int goodNodes(TreeNode root) {
      if (root == null) {
        return 0;
      }
      return dfs(root, root.val);
    }

    private int dfs(TreeNode root, int maxValue) {
      if (root == null) {
        return 0;
      }
      int curIsGoodNode = 0;
      if (root.val >= maxValue) {
        curIsGoodNode = 1;
        maxValue = root.val;
      }
      return curIsGoodNode + dfs(root.left, maxValue) + dfs(root.right, maxValue);
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
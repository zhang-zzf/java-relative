package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class MaximumDifferenceBetweenNodeAndAncestorTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        String tree = "[8,3,10,1,6,null,14,null,null,4,7,13]";
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final TreeNode root = codec.deserialize(tree);
        final int diff = solution.maxAncestorDiff(root);
        then(diff).isEqualTo(7);
    }

    // leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode right; TreeNode() {}
     * TreeNode(int val) { this.val = val; } TreeNode(int val, TreeNode left, TreeNode right) { this.val = val;
     * this.left = left; this.right = right; } }
     */
    class Solution {

        public int maxAncestorDiff(TreeNode root) {
            return dfs(root, root.val, root.val);
        }

        private int dfs(TreeNode root, int min, int max) {
            if (root == null) {
                return max - min;
            }
            if (root.val < min) {
                min = root.val;
            }
            else if (root.val > max) {
                max = root.val;
            }
            final int left = dfs(root.left, min, max);
            final int right = dfs(root.right, min, max);
            return Math.max(left, right);
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
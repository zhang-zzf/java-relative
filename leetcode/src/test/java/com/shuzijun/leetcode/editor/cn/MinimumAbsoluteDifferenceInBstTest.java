
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class MinimumAbsoluteDifferenceInBstTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        String tree = "[1,0,48,null,null,12,49]";
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final TreeNode root = codec.deserialize(tree);
        final int difference = solution.getMinimumDifference(root);
        then(difference).isEqualTo(1);
    }


    @Test
    public void givenFailedCaseOne_when_then() {
        String tree = "[236,104,701,null,227,null,911]";
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final TreeNode root = codec.deserialize(tree);
        final int ans = solution.getMinimumDifference(root);
        then(ans).isEqualTo(9);
    }

    //leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     * int val;
     * TreeNode left;
     * TreeNode right;
     * TreeNode() {}
     * TreeNode(int val) { this.val = val; }
     * TreeNode(int val, TreeNode left, TreeNode right) {
     * this.val = val;
     * this.left = left;
     * this.right = right;
     * }
     * }
     */
    class Solution {

        public int getMinimumDifference(TreeNode root) {
            int max = 10 * 10 * 10 * 10 * 10;
            return dfs(root, -max, max);
        }

        private int dfs(TreeNode root, int min, int max) {
            if (root == null) {
                return max - min;
            }
            int left = dfs(root.left, min, root.val);
            int right = dfs(root.right, root.val, max);
            return Math.min(left, right);
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
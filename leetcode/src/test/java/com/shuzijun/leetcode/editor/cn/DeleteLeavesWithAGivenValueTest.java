
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class DeleteLeavesWithAGivenValueTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        String tree = "[1,2,null,2,null,2]";
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final TreeNode root = codec.deserialize(tree);
        final TreeNode treeNode = solution.removeLeafNodes(root, 2);
        final String serialize = codec.serialize(treeNode);
        then(serialize).isEqualTo("[1]");
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

        public TreeNode removeLeafNodes(TreeNode root, int target) {
            final TreeNode dummy = new TreeNode(target - 1, root, null);
            dfs(dummy, target);
            return dummy.left;
        }

        private boolean dfs(TreeNode root, int target) {
            if (root == null) {
                return false;
            }
            final boolean left = dfs(root.left, target);
            final boolean right = dfs(root.right, target);
            if (left) {
                root.left = null;
            }
            if (right) {
                root.right = null;
            }
            if (root.left == null && root.right == null && root.val == target) {
                return true;
            }
            return false;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
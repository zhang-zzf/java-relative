
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


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

        public TreeNode pruneTree(TreeNode root) {
            final TreeNode dummy = new TreeNode(0, root, null);
            dfs(dummy);
            return dummy.left;
        }

        private boolean dfs(TreeNode root) {
            if (root == null) {
                return true;
            }
            boolean left = dfs(root.left);
            boolean right = dfs(root.right);
            if (left) {
                root.left = null;
            }
            if (right) {
                root.right = null;
            }
            return (root.val == 0) && left && right;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
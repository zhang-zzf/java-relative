
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class InvertBinaryTreeTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final TreeNode root = SerializeAndDeserializeBinaryTreeTest.deserialize("[4,2,7,1,3,6,9]");
        final TreeNode invertTree = solution.invertTree(root);
        final String serialize = SerializeAndDeserializeBinaryTreeTest.serialize(invertTree);
        then(serialize).isEqualTo("[4,7,2,9,6,3,1]");
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

        public TreeNode invertTree(TreeNode root) {
            if (root == null) {
                return null;
            }
            TreeNode tmp = root.left;
            root.left = root.right;
            root.right = tmp;
            invertTree(root.left);
            invertTree(root.right);
            return root;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
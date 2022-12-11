
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


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

        public boolean isValidBST(TreeNode root) {
            boolean ans = true;
            TreeNode prev = null;
            while (root != null) {
                if (root.left != null) {
                    // travel left tree
                    // find the predecessor
                    TreeNode predecessor = root.left;
                    while (predecessor.right != null && predecessor.right != root) {
                        predecessor = predecessor.right;
                    }
                    if (predecessor.right == null) {
                        predecessor.right = root;
                        root = root.left;
                        continue;
                    } else {
                        predecessor.right = null;
                    }
                }
                // left sub tree is null
                // inorder traversal
                if (prev != null && prev.val >= root.val) {
                    ans = false;
                    break;
                }
                prev = root;
                // travel right tree
                root = root.right;
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
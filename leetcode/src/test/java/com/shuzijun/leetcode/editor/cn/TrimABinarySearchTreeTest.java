
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


public class TrimABinarySearchTreeTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {

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

        public TreeNode trimBST(TreeNode root, int low, int high) {
            // 找到新的根节点
            while (root != null && (root.val < low || root.val > high)) {
                if (root.val < low) {
                    root = root.right;
                } else {
                    root = root.left;
                }
            }
            TreeNode treeRoot = root;
            // 干掉左子树中 < low
            while (root != null) {
                while (root.left != null && root.left.val < low) {
                    root.left = root.left.right;
                }
                root = root.left;
            }
            // 干掉右子树中 > high
            root = treeRoot;
            while (root != null) {
                while (root.right != null && root.right.val > high) {
                    root.right = root.right.left;
                }
                root = root.right;
            }
            return treeRoot;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
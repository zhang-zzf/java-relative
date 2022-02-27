
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.BDDAssertions.then;


public class ValidateBinarySearchTreeTest {

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
            if (root == null) {
                return true;
            }
            return midOrder(root, new AtomicLong(Long.MIN_VALUE));
        }

        private boolean midOrder(TreeNode root, AtomicLong max) {
            if (root == null) {
                return true;
            }
            if (!midOrder(root.left, max)) {
                return false;
            }
            if (root.val > max.get()) {
                max.set(root.val);
            } else {
                return false;
            }
            if (!midOrder(root.right, max)) {
                return false;
            }
            return true;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
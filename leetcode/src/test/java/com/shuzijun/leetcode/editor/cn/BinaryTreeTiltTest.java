
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


public class BinaryTreeTiltTest {

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

        public int findTilt(TreeNode root) {
            return dfs(root);
        }

        private int dfs(TreeNode root) {
            if (root == null) {
                return 0;
            }
            int ans = Math.abs(dfsInner(root.left) - dfsInner(root.right));
            return ans + dfs(root.left) + dfs(root.right);
        }

        private int dfsInner(TreeNode root) {
            if (root == null) {
                return 0;
            }
            return root.val + dfsInner(root.left) + dfsInner(root.right);
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
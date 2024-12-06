package com.shuzijun.leetcode.editor.cn;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;


public class BinaryTreeTiltTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {

    }

    // leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode right; TreeNode() {}
     * TreeNode(int val) { this.val = val; } TreeNode(int val, TreeNode left, TreeNode right) { this.val = val;
     * this.left = left; this.right = right; } }
     */
    class Solution {

        public int findTilt(TreeNode root) {
            AtomicInteger ans = new AtomicInteger(0);
            dfs(root, ans);
            return ans.get();
        }

        /**
         * 返回包含 root 在内的子树的和
         */
        private int dfs(TreeNode root, AtomicInteger ans) {
            if (root == null) {
                return 0;
            }
            int leftSum = dfs(root.left, ans);
            int rightSum = dfs(root.right, ans);
            ans.getAndAdd(Math.abs(leftSum - rightSum));
            // 返回子树的和
            return root.val + leftSum + rightSum;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
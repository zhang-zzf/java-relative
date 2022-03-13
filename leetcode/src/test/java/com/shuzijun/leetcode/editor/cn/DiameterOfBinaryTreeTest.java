
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.BDDAssertions.then;


class DiameterOfBinaryTreeTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final TreeNode root = SerializeAndDeserializeBinaryTreeTest.deserialize("[1,2,3,4,5]");
        final int diameterOfBinaryTree = solution.diameterOfBinaryTree(root);
        then(diameterOfBinaryTree).isEqualTo(3);
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

        public int diameterOfBinaryTree(TreeNode root) {
            AtomicInteger ans = new AtomicInteger(0);
            dfs(root, ans);
            return ans.get();
        }

        private int dfs(TreeNode root, AtomicInteger ans) {
            if (root == null) {
                return 0;
            }
            final int leftDiameter = dfs(root.left, ans);
            final int rightDiameter = dfs(root.right, ans);
            // 更新最大值
            ans.set(Math.max(leftDiameter + rightDiameter, ans.get()));
            return Math.max(leftDiameter, rightDiameter) + 1;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
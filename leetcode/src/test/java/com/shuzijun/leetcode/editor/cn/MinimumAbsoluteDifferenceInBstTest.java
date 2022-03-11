
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

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
            final AtomicInteger ans = new AtomicInteger(Integer.MAX_VALUE);
            dfs(root, null, null, ans);
            return ans.get();
        }

        private void dfs(TreeNode root, Integer min, Integer max, AtomicInteger ans) {
            if (root == null) {
                return;
            }
            if (root.left != null) {
                dfs(root.left, min, root.val, ans);
            }
            if (root.right != null) {
                dfs(root.right, root.val, max, ans);
            }
            int tmp;
            if (min != null && (tmp = root.val - min) < ans.get()) {
                ans.set(tmp);
            }
            if (max != null && (tmp = max - root.val) < ans.get()) {
                ans.set(tmp);
            }
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
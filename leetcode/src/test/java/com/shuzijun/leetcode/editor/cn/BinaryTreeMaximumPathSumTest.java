
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.BDDAssertions.then;


public class BinaryTreeMaximumPathSumTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        String tree = "[-10,9,20,null,null,15,7]";
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final TreeNode root = codec.deserialize(tree);
        final int maxPathSum = solution.maxPathSum(root);
        then(maxPathSum).isEqualTo(42);
    }

    @Test
    void givenFailedCaseOne_when_thenSuccess() {
        String tree = "[1,2,3]";
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final TreeNode root = codec.deserialize(tree);
        final int maxPathSum = solution.maxPathSum(root);
        then(maxPathSum).isEqualTo(6);
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

        public int maxPathSum(TreeNode root) {
            AtomicInteger ans = new AtomicInteger(root.val);
            dfs(root, ans);
            return ans.get();
        }

        /**
         * @return 返回包含 root 节点的左右子树最大路径和
         */
        private int dfs(TreeNode root, AtomicInteger ans) {
            if (root == null) {
                return 0;
            }
            final int left = dfs(root.left, ans);
            final int right = dfs(root.right, ans);
            // 顺便处理下结果值
            int rootMax = root.val;
            if (left > 0) {
                rootMax += left;
            }
            if (right > 0) {
                rootMax += right;
            }
            ans.set(Math.max(ans.get(), rootMax));
            // 返回包含 root 节点的最大路径和
            return Math.max(root.val, Math.max(root.val + left, root.val + right));
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
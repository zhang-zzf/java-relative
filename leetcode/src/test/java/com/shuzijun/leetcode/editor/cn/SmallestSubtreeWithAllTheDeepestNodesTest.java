package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;


public class SmallestSubtreeWithAllTheDeepestNodesTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        String tree = "[3,5,1,6,2,0,8,null,null,7,4,9]";
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final TreeNode root = codec.deserialize(tree);
        final TreeNode treeNode = solution.subtreeWithAllDeepest(root);
        then(treeNode.val).isEqualTo(3);
    }

    // leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode right; TreeNode() {}
     * TreeNode(int val) { this.val = val; } TreeNode(int val, TreeNode left, TreeNode right) { this.val = val;
     * this.left = left; this.right = right; } }
     */
    class Solution {

        public TreeNode subtreeWithAllDeepest(TreeNode root) {
            final AtomicReference<TreeNode> ans = new AtomicReference<>();
            final AtomicInteger maxDepth = new AtomicInteger(0);
            dfsMaxDepth(root, 1, maxDepth, ans);
            return ans.get();
        }

        private int dfsMaxDepth(TreeNode root, int depth, AtomicInteger maxDepth,
            AtomicReference<TreeNode> ans) {
            if (root == null) {
                return depth - 1;
            }
            int left = dfsMaxDepth(root.left, depth + 1, maxDepth, ans);
            int right = dfsMaxDepth(root.right, depth + 1, maxDepth, ans);
            if (left == right && left >= maxDepth.get()) {
                ans.set(root);
                maxDepth.set(left);
            }
            return Math.max(left, right);
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
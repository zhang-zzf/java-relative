
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

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
            Map<TreeNode, int[]> cache = new HashMap<TreeNode, int[]>();
            dfs(root, cache);
            int maxSum = root.val;
            Queue<TreeNode> queue = new LinkedList<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                final TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.add(node.left);
                    final int[] ints = cache.get(node.left);
                    int nodeMax = Math.max(ints[0], ints[1]);
                    final int parentMax = cache.get(node)[1];
                    if (parentMax > 0) {
                        nodeMax += parentMax;
                    }
                    maxSum = Math.max(maxSum, nodeMax);
                }
                if (node.right != null) {
                    queue.add(node.right);
                    final int[] ints = cache.get(node.right);
                    int nodeMax = Math.max(ints[0], ints[1]);
                    final int parentMax = cache.get(node)[0];
                    if (parentMax > 0) {
                        nodeMax += parentMax;
                    }
                    maxSum = Math.max(maxSum, nodeMax);
                }
            }
            return maxSum;
        }

        private void dfs(TreeNode root, Map<TreeNode, int[]> cache) {
            int leftMax = 0, rightMax = 0;
            if (root.left != null) {
                dfs(root.left, cache);
                final int[] ints = cache.get(root.left);
                leftMax = Math.max(ints[0], ints[1]);
                leftMax = Math.max(leftMax, 0);
            }
            if (root.right != null) {
                dfs(root.right, cache);
                final int[] ints = cache.get(root.right);
                rightMax = Math.max(ints[0], ints[1]);
                rightMax = Math.max(rightMax, 0);
            }
            cache.put(root, new int[]{leftMax + root.val, rightMax + root.val});
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
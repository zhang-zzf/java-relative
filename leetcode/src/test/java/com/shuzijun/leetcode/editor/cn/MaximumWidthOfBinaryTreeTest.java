package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.LinkedList;
import java.util.Queue;
import org.junit.jupiter.api.Test;


public class MaximumWidthOfBinaryTreeTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        String tree = "[1,3,2,5,3,null,9]";
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final TreeNode root = codec.deserialize(tree);
        final int width = solution.widthOfBinaryTree(root);
        then(width).isEqualTo(4);
    }

    // leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode right; TreeNode() {}
     * TreeNode(int val) { this.val = val; } TreeNode(int val, TreeNode left, TreeNode right) { this.val = val;
     * this.left = left; this.right = right; } }
     */
    class Solution {

        public int widthOfBinaryTree(TreeNode root) {
            int ans = 0;
            if (root == null) {
                return 0;
            }
            Queue<Item> queue = new LinkedList<>();
            queue.add(new Item(root, 0, 0));
            int curDepth = 0, left = 0;
            // BFS
            while (!queue.isEmpty()) {
                final Item item = queue.poll();
                if (curDepth != item.depth) {
                    curDepth = item.depth;
                    left = item.pos;
                }
                ans = Math.max(ans, item.pos - left + 1);
                final TreeNode node = item.node;
                if (node.left != null) {
                    queue.add(new Item(node.left, item.depth + 1, item.pos * 2 + 1));
                }
                if (node.right != null) {
                    queue.add(new Item(node.right, item.depth + 1, item.pos * 2 + 2));
                }
            }
            return ans;
        }

        class Item {

            TreeNode node;
            int depth;
            int pos;

            public Item(TreeNode root, int depth, int pos) {
                this.node = root;
                this.depth = depth;
                this.pos = pos;
            }

        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
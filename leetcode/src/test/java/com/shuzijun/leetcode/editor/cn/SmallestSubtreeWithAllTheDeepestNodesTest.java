
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


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

        public TreeNode subtreeWithAllDeepest(TreeNode root) {
            return dfs(root, null, 1).node;
        }

        private Item dfs(TreeNode root, TreeNode parent, int depth) {
            if (root == null) {
                return new Item(parent, depth);
            }
            final Item left = dfs(root.left, root, depth + 1);
            final Item right = dfs(root.right, root, depth + 1);
            if (left.depth == right.depth) {
                return new Item(root, left.depth);
            } else if (left.depth > right.depth) {
                return left;
            } else {
                return right;
            }
        }

        class Item {

            TreeNode node;
            int depth;

            public Item(TreeNode node, int depth) {
                this.node = node;
                this.depth = depth;
            }

        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
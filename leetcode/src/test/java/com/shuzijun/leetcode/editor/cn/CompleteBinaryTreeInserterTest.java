package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class CompleteBinaryTreeInserterTest {

    @Test
    void givenNormal_when_thenSuccess() {
        final TreeNode root = new TreeNode(1,
            new TreeNode(2,
                new TreeNode(4),
                new TreeNode(5)),
            new TreeNode(3));
        final CBTInserter cbtInserter = new CBTInserter(root);
        cbtInserter.insert(6);
        cbtInserter.insert(7);
        cbtInserter.insert(8);
        cbtInserter.insert(9);
        cbtInserter.insert(10);
        cbtInserter.insert(11);
        cbtInserter.insert(12);
        final TreeNode tree = cbtInserter.get_root();
        final String serialize = SerializeAndDeserializeBinaryTreeTest.serialize(tree);
        then(serialize).isEqualTo("[1,2,3,4,5,6,7,8]");
    }

    // leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode right; TreeNode() {}
     * TreeNode(int val) { this.val = val; } TreeNode(int val, TreeNode left, TreeNode right) { this.val = val;
     * this.left = left; this.right = right; } }
     */
    class CBTInserter {

        /**
         * 完全二叉树每个节点编号，从 1 开始
         * <p></p>
         */
        // 树根
        TreeNode root;
        int size;
        TreeNode nodeToInsert;

        public CBTInserter(TreeNode root) {
            this.root = root;
            this.size = dfsCountSize(root);
            this.nodeToInsert = findNodeByIndex((size + 1) / 2);
        }

        private int dfsCountSize(TreeNode root) {
            if (root == null) {
                return 0;
            }
            return 1 + dfsCountSize(root.left) + dfsCountSize(root.right);
        }

        public int insert(int val) {
            if (size == 0) {
                this.root = new TreeNode(val);
                this.nodeToInsert = this.root;
                return -1;
            }
            size += 1;
            if (size % 2 == 1) {
                nodeToInsert.right = new TreeNode(val);
            }
            else {
                this.nodeToInsert = findNodeByIndex(size / 2);
                this.nodeToInsert.left = new TreeNode(val);
            }
            return nodeToInsert.val;
        }

        private TreeNode findNodeByIndex(int idx) {
            if (idx < 1) {
                throw new IllegalArgumentException();
            }
            // 判断 idx 所在的层
            int h = 0;
            while ((1 << h) <= idx) {
                h += 1;
            }
            TreeNode ptr = root;
            for (int i = h - 2; i >= 0; i--) {
                // 注意： & 运算结果必须和 0 比较，不能和 1 比较
                if (((1 << i) & idx) == 0) {
                    ptr = ptr.left;
                }
                else {
                    ptr = ptr.right;
                }
            }
            return ptr;
        }

        public TreeNode get_root() {
            return root;
        }

    }

    /**
     * Your CBTInserter object will be instantiated and called as such:
     * CBTInserter obj = new CBTInserter(root);
     * int param_1 = obj.insert(val);
     * TreeNode param_2 = obj.get_root();
     */
    // leetcode submit region end(Prohibit modification and deletion)


}
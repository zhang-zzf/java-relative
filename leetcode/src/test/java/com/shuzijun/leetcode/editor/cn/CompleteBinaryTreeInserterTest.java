
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;


public class CompleteBinaryTreeInserterTest {

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
    class CBTInserter {

        final Queue<TreeNode> queue = new LinkedList<>();
        // 树根
        TreeNode root;

        public CBTInserter(TreeNode root) {
            if (root == null) {
                return;
            }
            this.root = root;
            queue.offer(root);
            while (!queue.isEmpty()) {
                final TreeNode peek = queue.peek();
                if (peek.left != null) {
                    queue.offer(peek.left);
                    if (peek.right != null) {
                        queue.offer(peek.right);
                        // 弹出
                        queue.poll();
                    } else {
                        // 没有右元素
                        break;
                    }
                } else {
                    // 没有左元素
                    break;
                }
            }
        }

        public int insert(int val) {
            final TreeNode peek = queue.peek();
            final TreeNode node = new TreeNode(val);
            if (this.root == null) {
                this.root = node;
            }
            queue.offer(node);
            if (peek.left == null) {
                peek.left = node;
            } else {
                peek.right = node;
                // 移除
                queue.poll();
            }
            return peek.val;
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
//leetcode submit region end(Prohibit modification and deletion)


}
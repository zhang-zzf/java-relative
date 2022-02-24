
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.assertj.core.api.BDDAssertions.then;


public class SerializeAndDeserializeBinaryTreeTest {

    final Codec solution = new Codec();

    @Test
    void givenNormal_when_thenSuccess() {
        final TreeNode root = new TreeNode(1,
                null,
                new TreeNode(2,
                        new TreeNode(3,
                                null,
                                new TreeNode(5)),
                        null));
        final String serialize = solution.serialize(root);
        then(serialize).isEqualTo("[1,null,2,3,null,null,5]");
        final TreeNode deserialize = solution.deserialize(serialize);
        then(deserialize.right.left.right.val).isEqualTo(5);
    }

    /**
     * [1,2,3,1,null,4,5,null,null,null,null,null,6]
     */
    @Test
    void givenFailedCaseOne_when_then() {
        String data = "[1,2,3,1,null,4,5,null,null,null,null,null,6]";
        final TreeNode deserialize = solution.deserialize(data);
        final String serialize = solution.serialize(deserialize);
        then(serialize).isEqualTo(data);
    }

    //leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     * int val;
     * TreeNode left;
     * TreeNode right;
     * TreeNode(int x) { val = x; }
     * }
     */
    public class Codec {

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            if (root == null) {
                return "[]";
            }
            StringBuilder buf = new StringBuilder("[");
            // append().append(',');
            bfs(root, buf);
            while (buf.length() >= 5 && buf.substring(buf.length() - 5).equals("null,")) {
                // 删除尾部
                buf.delete(buf.length() - 5, buf.length());
            }
            buf.delete(buf.length() - 1, buf.length());
            buf.append(']');
            return buf.toString();
        }

        private void bfs(TreeNode root, StringBuilder buf) {
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                final TreeNode poll = queue.poll();
                if (poll != null) {
                    buf.append(poll.val).append(',');
                    queue.add(poll.left);
                    queue.add(poll.right);
                } else {
                    buf.append("null,");
                }
            }
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            if (data == null || "[]".equals(data)) {
                return null;
            }
            final String[] values = data.substring(1, data.length() - 1).split(",");
            TreeNode root = new TreeNode(Integer.valueOf(values[0]));
            Queue<TreeNode> queue = new LinkedList<>();
            queue.add(root);
            for (int i = 1; i < values.length; i++) {
                final String value = values[i];
                TreeNode node = null;
                if (!"null".equals(value)) {
                    node = new TreeNode(Integer.valueOf(value));
                }
                TreeNode parent;
                if (i % 2 == 1) {
                    parent = queue.peek();
                    parent.left = node;
                } else {
                    parent = queue.poll();
                    parent.right = node;
                }
                if (node != null) {
                    queue.offer(node);
                }
            }
            return root;
        }

    }

// Your Codec object will be instantiated and called as such:
// Codec ser = new Codec();
// Codec deser = new Codec();
// TreeNode ans = deser.deserialize(ser.serialize(root));
//leetcode submit region end(Prohibit modification and deletion)


}
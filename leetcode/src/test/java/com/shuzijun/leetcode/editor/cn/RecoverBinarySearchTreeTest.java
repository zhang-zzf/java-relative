
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class RecoverBinarySearchTreeTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final TreeNode root = new TreeNode(3,
                new TreeNode(1),
                new TreeNode(2,
                        null,
                        new TreeNode(4))
        );
        solution.recoverTree(root);
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final String serialize = codec.serialize(root);
        then(serialize).isEqualTo("[2,1,3,null,null,null,4]");
    }

    /**
     * [4,2,3,1]
     * [2,4,3,null,null,null,1]
     */
    @Test
    void givenFailedCaseOne_when_then() {
        final TreeNode root = new TreeNode(2,
                new TreeNode(4),
                new TreeNode(3,
                        null,
                        new TreeNode(1))
        );
        solution.recoverTree(root);
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final String serialize = codec.serialize(root);
        then(serialize).isEqualTo("[2,1,3,null,null,null,4]");
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

        public void recoverTree(TreeNode root) {
            TreeNode prev = null, first = null, second = null;
            while (root != null) {
                if (root.left != null) {
                    // 找到左子树中最右节点
                    TreeNode predecessor = root.left;
                    while (predecessor.right != null
                            && predecessor.right != root) {
                        predecessor = predecessor.right;
                    }
                    // predecessor 的右子树为空，将其指向 root
                    if (predecessor.right == null) {
                        predecessor.right = root;
                        root = root.left;
                        continue;
                    } else {
                        // 复原
                        predecessor.right = null;
                    }
                }
                // 业务处理
                // 访问本节点
                if (prev != null && prev.val > root.val) {
                    if (first == null) {
                        first = prev;
                    }
                    second = root;
                }
                prev = root;
                // 业务处理
                // 转到右子树继续遍历
                root = root.right;
            }
            // swap
            int tmp = first.val;
            first.val = second.val;
            second.val = tmp;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}

package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class SuccessorLcciTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final TreeNode root = SerializeAndDeserializeBinaryTreeTest.deserialize(
                "[6,4,10,2,5,8,15,1,3,null,null,7,9,11,18]");
        final TreeNode successor = solution.inorderSuccessor(root, new TreeNode(12));
        then(successor.val).isEqualTo(15);
        final TreeNode predecessor = solution.inorderPredecessor(root, new TreeNode(12));
        then(predecessor.val).isEqualTo(11);
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
    class Solution {

        public TreeNode inorderPredecessor(TreeNode root, TreeNode p) {
            TreeNode ans = null;
            while (root != null) {
                if (root.val >= p.val) {
                    root = root.left;
                } else {
                    ans = root;
                    root = root.right;
                }
            }
            return ans;
        }

        public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
            TreeNode ans = null, prev = null;
            while (root != null) {
                // 遍历左子树
                if (root.left != null) {
                    TreeNode predecessor = root.left;
                    while (predecessor.right != null
                            && predecessor.right != root) {
                        predecessor = predecessor.right;
                    }
                    if (predecessor.right == null) {
                        predecessor.right = root;
                        root = root.left;
                        continue;
                    } else {
                        predecessor.right = null;
                    }
                }
                // 中序遍历处理 root 节点
                if (prev != null && prev.val == p.val) {
                    ans = root;
                    // break the loop
                    break;
                }
                prev = root;
                // 中序遍历处理 root 节点
                // 遍历右子树
                root = root.right;
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
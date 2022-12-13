
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class SuccessorLcciTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final TreeNode root = SerializeAndDeserializeBinaryTreeTest.deserialize(
                "[6,4,10,2,5,8,15,1,3,null,null,7,9,11,18]");
        then(solution.inorderSuccessor(root, new TreeNode(12)).val)
                .isEqualTo(15);
        then(solution.inorderPredecessor(root, new TreeNode(12)).val)
                .isEqualTo(11);
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
            if (root == null) {
                return null;
            }
            if (root.val >= p.val) {
                return inorderPredecessor(root.left, p);
            } else {
                TreeNode treeNode = inorderPredecessor(root.right, p);
                return treeNode == null ? root : treeNode;
            }
        }

        public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
            if (root == null) {
                return null;
            }
            if (root.val <= p.val) {
                return inorderSuccessor(root.right, p);
            } else {
                TreeNode successor = inorderSuccessor(root.left, p);
                return successor == null ? root : successor;
            }
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
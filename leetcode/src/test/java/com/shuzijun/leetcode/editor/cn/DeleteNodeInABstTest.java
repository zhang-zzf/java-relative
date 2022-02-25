//给定一个二叉搜索树的根节点 root 和一个值 key，删除二叉搜索树中的 key 对应的节点，并保证二叉搜索树的性质不变。返回二叉搜索树（有可能被更新）的
//根节点的引用。 
//
// 一般来说，删除节点可分为两个步骤： 
//
// 
// 首先找到需要删除的节点； 
// 如果找到了，删除它。 
// 
//
// 
//
// 示例 1: 
//
// 
//
// 
//输入：root = [5,3,6,2,4,null,7], key = 3
//输出：[5,4,6,2,null,null,7]
//解释：给定需要删除的节点值是 3，所以我们首先找到 3 这个节点，然后删除它。
//一个正确的答案是 [5,4,6,2,null,null,7], 如下图所示。
//另一个正确答案是 [5,2,6,null,4,null,7]。
//
//
// 
//
// 示例 2: 
//
// 
//输入: root = [5,3,6,2,4,null,7], key = 0
//输出: [5,3,6,2,4,null,7]
//解释: 二叉树不包含值为 0 的节点
// 
//
// 示例 3: 
//
// 
//输入: root = [], key = 0
//输出: [] 
//
// 
//
// 提示: 
//
// 
// 节点数的范围 [0, 10⁴]. 
// -10⁵ <= Node.val <= 10⁵ 
// 节点值唯一 
// root 是合法的二叉搜索树 
// -10⁵ <= key <= 10⁵ 
// 
//
// 
//
// 进阶： 要求算法时间复杂度为 O(h)，h 为树的高度。 
// Related Topics 树 二叉搜索树 二叉树 👍 646 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class DeleteNodeInABstTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final TreeNode root = new TreeNode(5,
                new TreeNode(3,
                        new TreeNode(2),
                        new TreeNode(4)),
                new TreeNode(6,
                        null,
                        new TreeNode(7))
        );
        final TreeNode node = solution.deleteNode(root, 3);
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final String serialize = codec.serialize(node);
        then(serialize).isEqualTo("[5,4,6,2,null,null,7]");
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

        public TreeNode deleteNode(TreeNode root, int key) {
            TreeNode treeRoot = root;
            TreeNode ptr = root, parent = null;
            while (ptr != null) {
                if (ptr.val == key) {
                    TreeNode newRoot = convertTree(ptr);
                    if (parent == null) {
                        treeRoot = newRoot;
                    } else if (ptr == parent.left) {
                        parent.left = newRoot;
                    } else {
                        parent.right = newRoot;
                    }
                    break;
                } else if (ptr.val < key) {
                    parent = ptr;
                    ptr = ptr.right;
                } else {
                    parent = ptr;
                    ptr = ptr.left;
                }
            }
            return treeRoot;
        }

        private TreeNode convertTree(TreeNode root) {
            if (root.left == null) {
                return root.right;
            }
            if (root.right == null) {
                return root.left;
            }
            // 左子树挂在右子树中最小的元素
            TreeNode ptr = root.right;
            while (ptr.left != null) {
                ptr = ptr.left;
            }
            ptr.left = root.left;
            return root.right;
        }

        private TreeNode recursion(TreeNode root, int key) {
            if (root == null) {
                return null;
            }
            if (root.val == key) {
                return convertTree(root);
            } else if (root.val < key) {
                root.right = recursion(root.right, key);
            } else {
                root.left = recursion(root.left, key);
            }
            return root;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
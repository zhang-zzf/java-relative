// 给定一个整数数组，它表示BST(即 二叉搜索树 )的 先序遍历 ，构造树并返回其根。
//
// 保证 对于给定的测试用例，总是有可能找到具有给定需求的二叉搜索树。 
//
// 二叉搜索树 是一棵二叉树，其中每个节点， Node.left 的任何后代的值 严格小于 Node.val , Node.right 的任何后代的值 严格大
// 于 Node.val。
//
// 二叉树的 前序遍历 首先显示节点的值，然后遍历Node.left，最后遍历Node.right。 
//
// 
//
// 示例 1： 
//
// 
//
// 
// 输入：preorder = [8,5,1,7,10,12]
// 输出：[8,5,10,1,7,null,12]
// 
//
// 示例 2: 
//
// 
// 输入: preorder = [1,3]
// 输出: [1,null,3]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= preorder.length <= 100 
// 1 <= preorder[i] <= 10^8 
// preorder 中的值 互不相同 
// 
//
// 
// Related Topics 栈 树 二叉搜索树 数组 二叉树 单调栈 👍 194 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class ConstructBinarySearchTreeFromPreorderTraversalTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final TreeNode root = solution.bstFromPreorder(new int[]{8, 5, 1, 7, 10, 12});
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final String serialize = codec.serialize(root);
        then(serialize).isEqualTo("[8,5,10,1,7,null,12]");
    }

    // leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode right; TreeNode() {}
     * TreeNode(int val) { this.val = val; } TreeNode(int val, TreeNode left, TreeNode right) { this.val = val;
     * this.left = left; this.right = right; } }
     */
    class Solution {

        public TreeNode bstFromPreorder(int[] preorder) {
            return recursion(preorder, 0, preorder.length - 1);
        }

        private TreeNode recursion(int[] preorder, int left, int right) {
            if (left > right) {
                return null;
            }
            int pivot = preorder[left];
            int mid = left;
            for (int i = left + 1; i <= right; i++) {
                if (preorder[i] < pivot) {
                    mid = i;
                }
                else {
                    break;
                }
            }
            return new TreeNode(pivot,
                recursion(preorder, left + 1, mid),
                recursion(preorder, mid + 1, right)
            );
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}

package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class MinimumAbsoluteDifferenceInBstTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        String tree = "[1,0,48,null,null,12,49]";
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final TreeNode root = codec.deserialize(tree);
        final int difference = solution.getMinimumDifference(root);
        then(difference).isEqualTo(1);
    }


    @Test
    public void givenFailedCaseOne_when_then() {
        String tree = "[236,104,701,null,227,null,911]";
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final TreeNode root = codec.deserialize(tree);
        final int ans = solution.getMinimumDifference(root);
        then(ans).isEqualTo(9);
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

        public int getMinimumDifference(TreeNode root) {
            int ans = Integer.MAX_VALUE;
            Integer prev = null;
            while (root != null) {
                if (root.left != null) {
                    // 存在左子树,遍历左子树
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
                // 中序遍历
                int tmp;
                if (prev != null && (tmp = root.val - prev) < ans) {
                    ans = tmp;
                }
                prev = root.val;
                // 遍历右子树
                root = root.right;
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
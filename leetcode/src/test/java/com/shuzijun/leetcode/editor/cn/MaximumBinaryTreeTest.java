
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class MaximumBinaryTreeTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final TreeNode root = solution.constructMaximumBinaryTree(new int[]{3, 2, 1, 6, 0, 5});
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final String serialize = codec.serialize(root);
        then(serialize).isEqualTo("[6,3,5,null,2,0,null,null,1]");
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

        public TreeNode constructMaximumBinaryTree(int[] nums) {
            return recursion(nums, 0, nums.length - 1);
        }

        private TreeNode recursion(int[] nums, int left, int right) {
            if (left > right) {
                return null;
            }
            int maxIdx = left;
            for (int i = left; i <= right; i++) {
                if (nums[i] > nums[maxIdx]) {
                    maxIdx = i;
                }
            }
            return new TreeNode(nums[maxIdx],
                    recursion(nums, left, maxIdx - 1),
                    recursion(nums, maxIdx + 1, right)
            );
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
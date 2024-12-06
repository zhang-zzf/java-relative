package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;


public class SumRootToLeafNumbersTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        String tree = "[4,9,0,5,1]";
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final TreeNode root = codec.deserialize(tree);
        final int numbers = solution.sumNumbers(root);
        then(numbers).isEqualTo(1026);
    }

    // leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode right; TreeNode() {}
     * TreeNode(int val) { this.val = val; } TreeNode(int val, TreeNode left, TreeNode right) { this.val = val;
     * this.left = left; this.right = right; } }
     */
    class Solution {

        public int sumNumbers(TreeNode root) {
            AtomicInteger ans = new AtomicInteger(0);
            dfs(root, 0, ans);
            return ans.get();
        }

        private void dfs(TreeNode root, int parentValue, AtomicInteger nums) {
            if (root == null) {
                return;
            }
            int curValue = parentValue * 10 + root.val;
            if (root.left == null && root.right == null) {
                nums.getAndAdd(curValue);
            }
            if (root.left != null) {
                dfs(root.left, curValue, nums);
            }
            if (root.right != null) {
                dfs(root.right, curValue, nums);
            }
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class PathsWithSumLcciTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final TreeNode root = new TreeNode(5,
            new TreeNode(4,
                new TreeNode(11,
                    new TreeNode(7),
                    new TreeNode(2)
                ),
                null),
            new TreeNode(8,
                new TreeNode(13),
                new TreeNode(4,
                    new TreeNode(5),
                    new TreeNode(1)))
        );
        final int sum = solution.pathSum(root, 22);
        then(sum).isEqualTo(3);
    }

    // leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode right; TreeNode(int
     * x) { val = x; } }
     */
    class Solution {

        public int pathSum(TreeNode root, int sum) {
            return dfs(root, sum);
        }

        private int dfs(TreeNode root, int sum) {
            if (root == null) {
                return 0;
            }
            return dfsInner(root, sum) + dfs(root.left, sum) + dfs(root.right, sum);
        }

        private int dfsInner(TreeNode root, int sum) {
            if (root == null) {
                return 0;
            }
            int ans = sum - root.val == 0 ? 1 : 0;
            return dfsInner(root.left, sum - root.val)
                + dfsInner(root.right, sum - root.val)
                + ans;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
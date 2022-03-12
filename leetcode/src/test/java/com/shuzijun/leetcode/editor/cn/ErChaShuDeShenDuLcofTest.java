
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


public class ErChaShuDeShenDuLcofTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {

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

        public int maxDepth(TreeNode root) {
            return dfsUpToDown(root, 0);
        }

        private int dfsUpToDown(TreeNode root, int depth) {
            // 先序遍历，up to down
            if (root == null) {
                return depth;
            }
            return Math.max(dfsUpToDown(root.left, depth + 1), dfsUpToDown(root.right, depth + 1));
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}

package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.BDDAssertions.then;


class FlipBinaryTreeToMatchPreorderTraversalTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final TreeNode root = SerializeAndDeserializeBinaryTreeTest.deserialize("[1,2,3]");
        final List<Integer> list = solution.flipMatchVoyage(root, new int[]{1, 3, 2});
        then(list).containsExactly(1);
    }

    @Test
    void givenNormalCase1_when_thenSuccess() {
        final TreeNode root = SerializeAndDeserializeBinaryTreeTest.deserialize("[1,2,3]");
        final List<Integer> list = solution.flipMatchVoyage(root, new int[]{1, 2, 3});
        then(list).isEmpty();
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

        public List<Integer> flipMatchVoyage(TreeNode root, int[] voyage) {
            List<Integer> ans = new ArrayList<>();
            if (dfs(root, voyage, new AtomicInteger(0), ans)) {
                return ans;
            }
            return Collections.singletonList(-1);
        }

        private boolean dfs(TreeNode root, int[] voyage, AtomicInteger idx, List<Integer> ans) {
            if (root == null) {
                return true;
            }
            if (root.val != voyage[idx.get()]) {
                return false;
            }
            // 每遍历一个节点，idx += 1
            final int nextIdx = idx.incrementAndGet();
            // 注意：若左子树为空，则不需要交换左右子树。（先序遍历的顺序： 根 -> 左 -> 右
            if (root.left != null && root.left.val != voyage[nextIdx]) {
                TreeNode node = root.left;
                root.left = root.right;
                root.right = node;
                ans.add(root.val);
            }
            return dfs(root.left, voyage, idx, ans)
                    && dfs(root.right, voyage, idx, ans);
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
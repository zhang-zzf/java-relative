package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


public class NumberOfGoodLeafNodesPairsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        String tree = "[1,2,3,4,5,6,7]";
        final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
        final TreeNode root = codec.deserialize(tree);
        final int pairs = solution.countPairs(root, 3);
        then(pairs).isEqualTo(2);
    }

    // leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode right; TreeNode() {}
     * TreeNode(int val) { this.val = val; } TreeNode(int val, TreeNode left, TreeNode right) { this.val = val;
     * this.left = left; this.right = right; } }
     */
    class Solution {

        public int countPairs(TreeNode root, int distance) {
            if (root == null) {
                return 0;
            }
            int ans = 0;
            final List<Integer> leftTrack = new ArrayList();
            dfs(root.left, distance, leftTrack);
            for (Integer left : leftTrack) {
                final List<Integer> rightTrack = new ArrayList();
                dfs(root.right, left, rightTrack);
                ans += rightTrack.size();
            }
            return ans
                + countPairs(root.left, distance)
                + countPairs(root.right, distance);
        }

        private void dfs(TreeNode root, int distance, List<Integer> track) {
            if (root == null || distance == 0) {
                return;
            }
            if (root.left != null) {
                dfs(root.left, distance - 1, track);
            }
            if (root.right != null) {
                dfs(root.right, distance - 1, track);
            }
            if (root.left == null && root.right == null) {
                track.add(distance - 1);
            }
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
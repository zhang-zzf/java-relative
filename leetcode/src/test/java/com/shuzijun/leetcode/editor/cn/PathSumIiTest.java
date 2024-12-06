// 给你二叉树的根节点 root 和一个整数目标和 targetSum ，找出所有 从根节点到叶子节点 路径总和等于给定目标和的路径。
//
// 叶子节点 是指没有子节点的节点。 
//
// 
// 
// 
//
// 示例 1： 
//
// 
// 输入：root = [5,4,8,11,null,13,4,7,2,null,null,5,1], targetSum = 22
// 输出：[[5,4,11,2],[5,8,4,5]]
// 
//
// 示例 2： 
//
// 
// 输入：root = [1,2,3], targetSum = 5
// 输出：[]
// 
//
// 示例 3： 
//
// 
// 输入：root = [1,2], targetSum = 0
// 输出：[]
// 
//
// 
//
// 提示： 
//
// 
// 树中节点总数在范围 [0, 5000] 内 
// -1000 <= Node.val <= 1000 
// -1000 <= targetSum <= 1000 
// 
// 
// 
// Related Topics 树 深度优先搜索 回溯 二叉树 👍 673 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


public class PathSumIiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final TreeNode root = new TreeNode(5,
            new TreeNode(4,
                new TreeNode(11,
                    new TreeNode(7), new TreeNode(2)),
                null),
            new TreeNode(8,
                new TreeNode(13),
                new TreeNode(4,
                    new TreeNode(5), new TreeNode(1))));
        final List<List<Integer>> ret = solution.pathSum(root, 22);
        then(ret).hasSize(2);
    }

    // leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode right; TreeNode() {}
     * TreeNode(int val) { this.val = val; } TreeNode(int val, TreeNode left, TreeNode right) { this.val = val;
     * this.left = left; this.right = right; } }
     */
    class Solution {

        public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
            List<List<Integer>> path = new ArrayList(0);
            if (root == null) {
                return path;
            }
            if (root.left == null && root.right == null) {
                // 叶子节点
                if (root.val == targetSum) {
                    path.add(new ArrayList<Integer>() {{
                        add(root.val);
                    }});
                }
                return path;
            }
            // 左 -> 右 -> 本节点 典型后序遍历
            if (root.left != null) {
                path.addAll(pathSum(root.left, targetSum - root.val));
            }
            if (root.right != null) {
                path.addAll(pathSum(root.right, targetSum - root.val));
            }
            for (List<Integer> list : path) {
                list.add(0, root.val);
            }
            return path;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;


@Slf4j
public class CountCompleteTreeNodesTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    String tree = "[1,2,3,4,5,6]";
    final SerializeAndDeserializeBinaryTreeTest.Codec codec = new SerializeAndDeserializeBinaryTreeTest().new Codec();
    final TreeNode root = codec.deserialize(tree);
    final int countNodes = solution.countNodes(root);
    then(countNodes).isEqualTo(6);
  }

  //leetcode submit region begin(Prohibit modification and deletion)

  /**
   * Definition for a binary tree node. public class TreeNode { int val; TreeNode left; TreeNode
   * right; TreeNode() {} TreeNode(int val) { this.val = val; } TreeNode(int val, TreeNode left,
   * TreeNode right) { this.val = val; this.left = left; this.right = right; } }
   */
  class Solution {

    public int countNodes(TreeNode root) {
      int ans = 0;
      int h = getHeight(root);
      if (h == 0) {
        return ans;
      }
      // 2分值域范围 [2^(h-1)+1, 2^h]
      int left = 1 << (h - 1), right = (1 << h) - 1;
      while (left <= right) {
        int mid = left + ((right - left) >> 1);
        if (guessRight(root, h, mid)) {
          left = mid + 1;
          ans = mid;
        } else {
          right = mid - 1;
        }
      }
      return ans;
    }

    private boolean guessRight(TreeNode root, int h, int length) {
      for (int i = h - 2; i >= 0; i--) {
        if ((length & (1 << i)) == 0) {
          root = root.left;
        } else {
          root = root.right;
        }
      }
      return root != null;
    }

    private int getHeight(TreeNode root) {
      int answer = 0;
      while (root != null) {
        answer += 1;
        root = root.left;
      }
      return answer;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
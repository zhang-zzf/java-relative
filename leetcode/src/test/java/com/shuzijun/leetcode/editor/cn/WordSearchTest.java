//给定一个 m x n 二维字符网格 board 和一个字符串单词 word 。如果 word 存在于网格中，返回 true ；否则，返回 false 。 
//
// 单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。同一个单元格内的字母不允许被重复使用。 
//
// 
//
// 示例 1： 
//
// 
//输入：board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = 
//"ABCCED"
//输出：true
// 
//
// 示例 2： 
//
// 
//输入：board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = 
//"SEE"
//输出：true
// 
//
// 示例 3： 
//
// 
//输入：board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = 
//"ABCB"
//输出：false
// 
//
// 
//
// 提示： 
//
// 
// m == board.length 
// n = board[i].length 
// 1 <= m, n <= 6 
// 1 <= word.length <= 15 
// board 和 word 仅由大小写英文字母组成 
// 
//
// 
//
// 进阶：你可以使用搜索剪枝的技术来优化解决方案，使其在 board 更大的情况下可以更快解决问题？ 
// Related Topics 数组 回溯 矩阵 👍 1171 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;


public class WordSearchTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final boolean exist = solution.exist(new char[][]{
                new char[]{'A', 'B', 'C', 'E'},
                new char[]{'S', 'F', 'C', 'S'},
                new char[]{'A', 'D', 'E', 'E'},

        }, "SEE");
        then(exist).isTrue();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public boolean exist(char[][] board, String word) {
            List<List<int[]>> matched = new ArrayList<>();
            backTrack(board, word, new ArrayList<int[]>(2), matched);
            return !matched.isEmpty();
        }

        final List<int[]> linked = new ArrayList<int[]>(4) {{
            add(new int[]{-1, 0});
            add(new int[]{0, 1});
            add(new int[]{1, 0});
            add(new int[]{0, -1});
        }};

        private void backTrack(char[][] board, String word, List<int[]> track, List<List<int[]>> matched) {
            if (track.isEmpty()) {
                // 找第一个匹配元素
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[i].length; j++) {
                        if (board[i][j] == word.charAt(0)) {
                            track.add(new int[]{i, j});
                            backTrack(board, word, track, matched);
                            track.remove(track.size() - 1);
                        }
                    }
                }
            } else {
                if (track.size() == word.length()) {
                    matched.add(new ArrayList<>(track));
                    return;
                }
                final int[] curNode = track.get(track.size() - 1);
                for (int[] ints : linked) {
                    final int[] node = {curNode[0] + ints[0], curNode[1] + ints[1]};
                    if (isValid(node, board.length, board[0].length)
                            && board[node[0]][node[1]] == word.charAt(track.size())
                            && !trackContainsNode(track, node)) {
                        track.add(node);
                        backTrack(board, word, track, matched);
                        track.remove(track.size() - 1);
                    }
                }
            }
        }

        private boolean trackContainsNode(List<int[]> track, int[] node) {
            for (int[] ints : track) {
                if (ints[0] == node[0] && ints[1] == node[1]) {
                    return true;
                }
            }
            return false;
        }

        private boolean isValid(int[] node, int rMax, int cMax) {
            if (node[0] < 0 || node[1] < 0) {
                return false;
            }
            if (node[0] >= rMax || node[1] >= cMax) {
                return false;
            }
            return true;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
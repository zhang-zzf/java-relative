//你正在参加一个多角色游戏，每个角色都有两个主要属性：攻击 和 防御 。给你一个二维整数数组 properties ，其中 properties[i] = [
//attacki, defensei] 表示游戏中第 i 个角色的属性。
//
// 如果存在一个其他角色的攻击和防御等级 都严格高于 该角色的攻击和防御等级，则认为该角色为 弱角色 。更正式地，如果认为角色 i 弱于 存在的另一个角色
//j ，那么 attackj > attacki 且 defensej > defensei 。
//
// 返回 弱角色 的数量。
//
//
//
// 示例 1：
//
//
//输入：properties = [[5,5],[6,3],[3,6]]
//输出：0
//解释：不存在攻击和防御都严格高于其他角色的角色。
//
//
// 示例 2：
//
//
//输入：properties = [[2,2],[3,3]]
//输出：1
//解释：第一个角色是弱角色，因为第二个角色的攻击和防御严格大于该角色。
//
//
// 示例 3：
//
//
//输入：properties = [[1,5],[10,4],[4,3]]
//输出：1
//解释：第三个角色是弱角色，因为第二个角色的攻击和防御严格大于该角色。
//
//
//
//
// 提示：
//
//
// 2 <= properties.length <= 10⁵
// properties[i].length == 2
// 1 <= attacki, defensei <= 10⁵
//
// Related Topics 栈 贪心 数组 排序 单调栈 👍 99 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.BDDAssertions.then;


public class TheNumberOfWeakCharactersInTheGameTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.numberOfWeakCharacters(new int[][]{
                new int[]{2, 2},
                new int[]{3, 6}
        })).isEqualTo(1);
        then(solution.numberOfWeakCharacters(new int[][]{
                new int[]{5, 5},
                new int[]{6, 3},
                new int[]{3, 6}
        })).isEqualTo(0);
        then(solution.numberOfWeakCharacters(new int[][]{
                new int[]{1, 3},
                new int[]{1, 4},
                new int[]{10, 4},
                new int[]{4, 5},
                new int[]{4, 4}
        })).isEqualTo(2);
        then(solution.numberOfWeakCharacters(new int[][]{
                new int[]{7, 9},
                new int[]{10, 7},
                new int[]{6, 9},
                new int[]{10, 4},
                new int[]{7, 5},
                new int[]{7, 10}
        })).isEqualTo(2);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int numberOfWeakCharacters(int[][] properties) {
            Arrays.sort(properties, (p1, p2) -> {
                // 攻击力大到小排列
                // 攻击力相同防御力小到大排列
                if (p1[0] == p2[0]) {
                    return p1[1] - p2[1];
                } else {
                    return p2[0] - p1[0];
                }
            });
            int[] defenceMax = new int[2];
            int ret = 0;
            for (int i = 0; i < properties.length; i++) {
                if (properties[i][1] > defenceMax[1]) {
                    // 绝不是弱角色
                    defenceMax = properties[i];
                } else if (properties[i][0] < defenceMax[0] && properties[i][1] < defenceMax[1]) {
                    ret += 1;
                }
            }
            return ret;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
//一只青蛙一次可以跳上1级台阶，也可以跳上2级台阶。求该青蛙跳上一个 n 级的台阶总共有多少种跳法。 
//
// 答案需要取模 1e9+7（1000000007），如计算初始结果为：1000000008，请返回 1。 
//
// 示例 1： 
//
// 输入：n = 2
//输出：2
// 
//
// 示例 2： 
//
// 输入：n = 7
//输出：21
// 
//
// 示例 3： 
//
// 输入：n = 0
//输出：1 
//
// 提示： 
//
// 
// 0 <= n <= 100 
// 
//
// 注意：本题与主站 70 题相同：https://leetcode-cn.com/problems/climbing-stairs/ 
//
// 
// Related Topics 记忆化搜索 数学 动态规划 👍 201 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class QingWaTiaoTaiJieWenTiLcofTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int numWays = solution.numWays(4);
        then(numWays).isEqualTo(5);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int numWays(int n) {
            // a 表示当前
            int a = 1;
            // b a的前一个
            int b = 1;
            // a 的后一个
            int sum;
            for (int i = 0; i < n - 1; i++) {
                sum = (a + b) % 1000000007;
                b = a;
                a = sum;
            }
            return a;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
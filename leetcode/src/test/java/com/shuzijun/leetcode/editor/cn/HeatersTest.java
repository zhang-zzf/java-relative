// 冬季已经来临。 你的任务是设计一个有固定加热半径的供暖器向所有房屋供暖。
//
// 在加热器的加热半径范围内的每个房屋都可以获得供暖。 
//
// 现在，给出位于一条水平线上的房屋 houses 和供暖器 heaters 的位置，请你找出并返回可以覆盖所有房屋的最小加热半径。 
//
// 说明：所有供暖器都遵循你的半径标准，加热的半径也一样。 
//
// 
//
// 示例 1: 
//
// 
// 输入: houses = [1,2,3], heaters = [2]
// 输出: 1
// 解释: 仅在位置2上有一个供暖器。如果我们将加热半径设为1，那么所有房屋就都能得到供暖。
// 
//
// 示例 2: 
//
// 
// 输入: houses = [1,2,3,4], heaters = [1,4]
// 输出: 1
// 解释: 在位置1, 4上有两个供暖器。我们需要将加热半径设为1，这样所有房屋就都能得到供暖。
// 
//
// 示例 3： 
//
// 
// 输入：houses = [1,5], heaters = [2]
// 输出：3
// 
//
// 
//
// 提示： 
//
// 
// 1 <= houses.length, heaters.length <= 3 * 10⁴ 
// 1 <= houses[i], heaters[i] <= 10⁹ 
// 
// Related Topics 数组 双指针 二分查找 排序 👍 373 👎 0


package com.shuzijun.leetcode.editor.cn;

import java.util.Arrays;
import org.junit.jupiter.api.Test;


public class HeatersTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        solution.findRadius(new int[]{1, 2, 3}, new int[]{2});
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int findRadius(int[] houses, int[] heaters) {
            // 排序
            Arrays.sort(heaters);
            int ans = 0;
            for (int house : houses) {
                int leftHeater = binarySearch(heaters, house);
                int leftRadius = (leftHeater == -1) ?
                    Integer.MAX_VALUE : (house - heaters[leftHeater]);
                int rightRadius = (leftHeater + 1 >= heaters.length) ?
                    Integer.MAX_VALUE : heaters[leftHeater + 1] - house;
                ans = Math.max(ans, Math.min(leftRadius, rightRadius));
            }
            return ans;
        }

        // 找出 <= house 的最大 heater
        private int binarySearch(int[] heaters, int house) {
            int ans = -1;
            int l = 0, r = heaters.length - 1;
            while (l <= r) {
                int m = l + ((r - l) >> 1);
                if (heaters[m] <= house) {
                    ans = m;
                    l = m + 1;
                }
                else {
                    r = m - 1;
                }
            }
            return ans;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
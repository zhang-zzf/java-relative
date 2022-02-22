//冬季已经来临。 你的任务是设计一个有固定加热半径的供暖器向所有房屋供暖。 
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
//输入: houses = [1,2,3], heaters = [2]
//输出: 1
//解释: 仅在位置2上有一个供暖器。如果我们将加热半径设为1，那么所有房屋就都能得到供暖。
// 
//
// 示例 2: 
//
// 
//输入: houses = [1,2,3,4], heaters = [1,4]
//输出: 1
//解释: 在位置1, 4上有两个供暖器。我们需要将加热半径设为1，这样所有房屋就都能得到供暖。
// 
//
// 示例 3： 
//
// 
//输入：houses = [1,5], heaters = [2]
//输出：3
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

import org.junit.jupiter.api.Test;


public class HeatersTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        solution.findRadius(new int[]{1, 2, 3}, new int[]{2});
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int findRadius(int[] houses, int[] heaters) {
            // 根据题目 解的值空间为 [0, 10⁹]
            int l = 0, r = 1000000000;
            while (l < r) {
                int m = l + ((r - l) >> 1);
                if (coverAllHouses(houses, heaters, m)) {
                    r = m;
                } else {
                    l = m + 1;
                }
            }
            return l;
        }

        private boolean coverAllHouses(int[] houses, int[] heaters, int radius) {
            int heaterIdx = 0;
            for (int i = 0; i < houses.length; i++) {
                int house = houses[i];
                // 采用 - 法，注意数越界
                // int leftBoarder = heaters[heaterIdx] - radius
                if (house < heaters[heaterIdx] - radius) {
                    return false;
                }
                // int rightBoarder = heaters[heaterIdx] + radius;
                if (house - heaters[heaterIdx] > radius) {
                    // 重要：有上界
                    if (heaterIdx + 1 >= heaters.length) {
                        return false;
                    }
                    heaterIdx += 1;
                    // 再来一次，判断下一个 heater 是否可以覆盖此 house
                    i -= 1;
                }
            }
            return true;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
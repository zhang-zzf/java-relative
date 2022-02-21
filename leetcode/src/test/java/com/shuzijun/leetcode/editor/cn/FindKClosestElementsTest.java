//给定一个 排序好 的数组 arr ，两个整数 k 和 x ，从数组中找到最靠近 x（两数之差最小）的 k 个数。返回的结果必须要是按升序排好的。 
//
// 整数 a 比整数 b 更接近 x 需要满足： 
//
// 
// |a - x| < |b - x| 或者 
// |a - x| == |b - x| 且 a < b 
// 
//
// 
//
// 示例 1： 
//
// 
//输入：arr = [1,2,3,4,5], k = 4, x = 3
//输出：[1,2,3,4]
// 
//
// 示例 2： 
//
// 
//输入：arr = [1,2,3,4,5], k = 4, x = -1
//输出：[1,2,3,4]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= k <= arr.length 
// 1 <= arr.length <= 10⁴ 
// arr 按 升序 排列 
// -10⁴ <= arr[i], x <= 10⁴ 
// 
// Related Topics 数组 双指针 二分查找 排序 堆（优先队列） 👍 296 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;


public class FindKClosestElementsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int[] arr = {1, 2, 3, 4, 5};
        then(solution.findClosestElements(arr, 4, 3)).containsExactly(1, 2, 3, 4);
        then(solution.findClosestElements(arr, 4, -1)).containsExactly(1, 2, 3, 4);
        then(solution.findClosestElements(new int[]{1, 1, 1, 10, 10, 10}, 1, 9)).containsExactly(10);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public List<Integer> findClosestElements(int[] arr, int k, int x) {
            final LinkedList<Integer> ret = new LinkedList<Integer>();
            // 找到第一个 <x 的下标
            int idx = -1;
            int left = 0, right = arr.length - 1;
            while (left < right) {
                int mid = left + ((right - left) >> 1) + 1;
                if (arr[mid] >= x) {
                    right = mid - 1;
                } else {
                    left = mid;
                }
            }
            if (arr[left] <= x) {
                idx = left;
            }
            // 左侧第一个元素
            left = idx;
            // 右侧第一个元素
            right = idx + 1;
            while (k-- > 0) {
                if (right >= arr.length) {
                    ret.addFirst(arr[left--]);
                } else if (left < 0) {
                    ret.addLast(arr[right++]);
                } else {
                    if (x - arr[left] <= arr[right] - x) {
                        ret.addFirst(arr[left--]);
                    } else {
                        ret.addLast(arr[right++]);
                    }
                }
            }
            return ret;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
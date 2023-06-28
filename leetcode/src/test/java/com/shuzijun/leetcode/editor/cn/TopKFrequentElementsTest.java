//给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按 任意顺序 返回答案。 
//
// 
//
// 示例 1: 
//
// 
//输入: nums = [1,1,1,2,2,3], k = 2
//输出: [1,2]
// 
//
// 示例 2: 
//
// 
//输入: nums = [1], k = 1
//输出: [1] 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 10⁵ 
// k 的取值范围是 [1, 数组中不相同的元素的个数] 
// 题目数据保证答案唯一，换句话说，数组中前 k 个高频元素的集合是唯一的 
// 
//
// 
//
// 进阶：你所设计算法的时间复杂度 必须 优于 O(n log n) ，其中 n 是数组大小。 
// Related Topics 数组 哈希表 分治 桶排序 计数 快速选择 排序 堆（优先队列） 👍 1021 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import org.junit.jupiter.api.Test;


public class TopKFrequentElementsTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final int[] topKFrequent = solution.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2);
    then(topKFrequent).contains(1, 2);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int[] topKFrequent(int[] nums, int k) {
      Map<Integer, Integer> numToRate = new HashMap<>(nums.length);
      for (int i = 0; i < nums.length; i++) {
        final int num = nums[i];
        numToRate.put(num, numToRate.getOrDefault(num, 0) + 1);
      }
      PriorityQueue<int[]> pq = new PriorityQueue<>(k + 1,
          Comparator.comparingInt(arr -> arr[0]));
      for (Map.Entry<Integer, Integer> e : numToRate.entrySet()) {
        final Integer value = e.getValue();
        if (pq.size() < k) {
          pq.add(new int[]{value, e.getKey()});
        } else if (value > pq.peek()[0]) {
          pq.add(new int[]{value, e.getKey()});
          pq.poll();
        }
      }
      final int[] ret = new int[k];
      while (!pq.isEmpty()) {
        ret[--k] = pq.poll()[1];
      }
      return ret;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
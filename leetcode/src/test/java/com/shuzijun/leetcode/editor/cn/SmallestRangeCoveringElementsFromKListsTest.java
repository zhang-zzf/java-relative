//你有 k 个 非递减排列 的整数列表。找到一个 最小 区间，使得 k 个列表中的每个列表至少有一个数包含在其中。 
//
// 我们定义如果 b-a < d-c 或者在 b-a == d-c 时 a < c，则区间 [a,b] 比 [c,d] 小。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [[4,10,15,24,26], [0,9,12,20], [5,18,22,30]]
//输出：[20,24]
//解释： 
//列表 1：[4, 10, 15, 24, 26]，24 在区间 [20,24] 中。
//列表 2：[0, 9, 12, 20]，20 在区间 [20,24] 中。
//列表 3：[5, 18, 22, 30]，22 在区间 [20,24] 中。
// 
//
// 示例 2： 
//
// 
//输入：nums = [[1,2,3],[1,2,3],[1,2,3]]
//输出：[1,1]
// 
//
// 
//
// 提示： 
//
// 
// nums.length == k 
// 1 <= k <= 3500 
// 1 <= nums[i].length <= 50 
// -10⁵ <= nums[i][j] <= 10⁵ 
// nums[i] 按非递减顺序排列 
// 
//
// 
// Related Topics 贪心 数组 哈希表 排序 滑动窗口 堆（优先队列） 👍 339 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;


public class SmallestRangeCoveringElementsFromKListsTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    List<List<Integer>> list = new ArrayList<List<Integer>>(8) {{
      add(Arrays.stream(new int[]{4, 10, 15, 24, 26})
          .mapToObj(Integer::new).collect(Collectors.toList()));
      add(Arrays.stream(new int[]{0, 9, 12, 20})
          .mapToObj(Integer::new).collect(Collectors.toList()));
      add(Arrays.stream(new int[]{5, 18, 22, 30})
          .mapToObj(Integer::new).collect(Collectors.toList()));
    }};
    final int[] result = solution.smallestRange(list);
    then(result).contains(20, 24);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int[] smallestRange(List<List<Integer>> nums) {
      List<int[]> sortedList = new ArrayList<>();
      PriorityQueue<int[]> pq = new PriorityQueue<>(nums.size(),
          Comparator.comparing(arr -> arr[0]));
      for (int i = 0; i < nums.size(); i++) {
        final List<Integer> integers = nums.get(i);
        if (integers.size() > 0) {
          pq.add(new int[]{integers.get(0), 0, i});
        }
      }
      while (!pq.isEmpty()) {
        final int[] poll = pq.poll();
        sortedList.add(poll);
        final List<Integer> integers = nums.get(poll[2]);
        final int nextIdx = poll[1] + 1;
        if (nextIdx < integers.size()) {
          pq.add(new int[]{integers.get(nextIdx), nextIdx, poll[2]});
        }
      }
      int[] period = null;
      Map<Integer, Boolean> existList = new HashMap<>(nums.size());
      for (int i = 0; i < sortedList.size(); i++) {
        for (int j = i; j < sortedList.size(); j++) {
          existList.put(sortedList.get(j)[2], Boolean.TRUE);
          if (existList.size() == nums.size()) {
            // 包含所有的列表
            final int[] newPeriod = {sortedList.get(i)[0], sortedList.get(j)[0]};
            if (period == null) {
              period = newPeriod;
            } else {
              if (newPeriod[1] - newPeriod[0] < period[1] - period[0]) {
                period = newPeriod;
              }
            }
            // 下次循环使用
            existList.clear();
            // 跳出当前循环
            break;
          }
        }
      }
      return period;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}
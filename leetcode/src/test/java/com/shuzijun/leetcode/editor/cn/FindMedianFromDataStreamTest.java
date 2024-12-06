// 中位数是有序列表中间的数。如果列表长度是偶数，中位数则是中间两个数的平均值。
//
// 例如， 
//
// [2,3,4] 的中位数是 3 
//
// [2,3] 的中位数是 (2 + 3) / 2 = 2.5 
//
// 设计一个支持以下两种操作的数据结构： 
//
// 
// void addNum(int num) - 从数据流中添加一个整数到数据结构中。 
// double findMedian() - 返回目前所有元素的中位数。 
// 
//
// 示例： 
//
// addNum(1)
// addNum(2)
// findMedian() -> 1.5
// addNum(3)
// findMedian() -> 2
//
// 进阶: 
//
// 
// 如果数据流中所有整数都在 0 到 100 范围内，你将如何优化你的算法？ 
// 如果数据流中 99% 的整数都在 0 到 100 范围内，你将如何优化你的算法？ 
// 
// Related Topics 设计 双指针 数据流 排序 堆（优先队列） 👍 632 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Comparator;
import java.util.PriorityQueue;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;


public class FindMedianFromDataStreamTest {

    final MedianFinder solution = new MedianFinder();

    @Test
    void givenNormal_when_thenSuccess() {
        int[] data = {-1, -2, -3, -4, -5};
        double[] result = {-1.0, -1.5D, -2.0, -2.5, -3.0};
        final double median = solution.findMedian();
        then(median).isCloseTo(0, Offset.offset(0.1));
        for (int i = 0; i < data.length; i++) {
            solution.addNum(data[i]);
            then(solution.findMedian()).isCloseTo(result[i], Offset.offset(0.1));
        }
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class MedianFinder {

        // <= k
        final PriorityQueue<Integer> pqMax = new PriorityQueue<>(Comparator.reverseOrder());
        // > k
        final PriorityQueue<Integer> pqMin = new PriorityQueue<>();
        private int size;

        public MedianFinder() {

        }

        public void addNum(int num) {
            pqMax.add(num);
            if (pqMin.peek() != null && pqMax.peek() > pqMin.peek()) {
                pqMax.add(pqMin.poll());
            }
            while (pqMax.size() - pqMin.size() > 1) {
                pqMin.add(pqMax.poll());
            }
            size += 1;
        }

        public double findMedian() {
            if (size == 0) {
                return 0D;
            }
            if (size % 2 == 0) {
                return (pqMax.peek() + pqMin.peek()) / 2d;
            }
            return pqMax.peek();
        }

    }

    /**
     * Your MedianFinder object will be instantiated and called as such:
     * MedianFinder obj = new MedianFinder();
     * obj.addNum(num);
     * double param_2 = obj.findMedian();
     */
    // leetcode submit region end(Prohibit modification and deletion)


}
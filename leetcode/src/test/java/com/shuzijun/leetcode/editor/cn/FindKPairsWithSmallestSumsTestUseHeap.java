// 给定两个以 升序排列 的整数数组 nums1 和 nums2 , 以及一个整数 k 。
//
// 定义一对值 (u,v)，其中第一个元素来自 nums1，第二个元素来自 nums2 。 
//
// 请找到和最小的 k 个数对 (u1,v1), (u2,v2) ... (uk,vk) 。 
//
// 
//
// 示例 1: 
//
// 
// 输入: nums1 = [1,7,11], nums2 = [2,4,6], k = 3
// 输出: [1,2],[1,4],[1,6]
// 解释: 返回序列中的前 3 对数：
//     [1,2],[1,4],[1,6],[7,2],[7,4],[11,2],[7,6],[11,4],[11,6]
// 
//
// 示例 2: 
//
// 
// 输入: nums1 = [1,1,2], nums2 = [1,2,3], k = 2
// 输出: [1,1],[1,1]
// 解释: 返回序列中的前 2 对数：
//     [1,1],[1,1],[1,2],[2,1],[1,2],[2,2],[1,3],[1,3],[2,3]
// 
//
// 示例 3: 
//
// 
// 输入: nums1 = [1,2], nums2 = [3], k = 3
// 输出: [1,3],[2,3]
// 解释: 也可能序列中所有的数对都被返回:[1,3],[2,3]
// 
//
// 
//
// 提示: 
//
// 
// 1 <= nums1.length, nums2.length <= 10⁵ 
// -10⁹ <= nums1[i], nums2[i] <= 10⁹ 
// nums1 和 nums2 均为升序排列 
// 1 <= k <= 1000 
// 
// Related Topics 数组 堆（优先队列） 👍 368 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


public class FindKPairsWithSmallestSumsTestUseHeap {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final Integer[] integers = {4, 6, 8, 2, 1};
        final Heap minHeap = new Heap<Integer>(-1, integers.length).initData(integers);
        then(minHeap.pop()).isEqualTo(1);
        then(minHeap.pop()).isEqualTo(2);
        then(minHeap.pop()).isEqualTo(4);
        then(minHeap.pop()).isEqualTo(6);
        then(minHeap.pop()).isEqualTo(8);
        then(new Heap<Integer>(1, integers.length).initData(integers).peek()).isEqualTo(8);
        //
        final List<List<Integer>> lists = solution.kSmallestPairs(new int[]{1, 7, 11},
            new int[]{2, 4, 6}, 3);
        then(lists).hasSize(3);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Heap<T extends Comparable<T>> {

        /**
         * -1 小堆 1 大堆
         */
        private final int heapType;
        private final int capacity;
        private int size;
        private T[] data;

        public Heap(int heapType, int capacity) {
            this.heapType = heapType;
            this.capacity = capacity;
            data = (T[]) new Comparable[capacity];
        }

        public Heap<T> initData(T[] data) {
            if (data.length > capacity) {
                throw new IllegalArgumentException();
            }
            for (T t : data) {
                this.data[size++] = t;
                siftUp(size - 1);
            }
            return this;
        }

        private void siftUp(int idx) {
            T tmp = data[idx];
            int parent = (idx - 1) / 2;
            while (idx > 0) {
                if (needSift(tmp, data[parent])) {
                    data[idx] = data[parent];
                    idx = parent;
                    parent = (idx - 1) / 2;
                }
                else {
                    break;
                }
            }
            data[idx] = tmp;
        }

        public T peek() {
            return data[0];
        }

        public T pop() {
            if (size <= 0) {
                return null;
            }
            T ret = data[0];
            data[0] = data[--size];
            siftDown(0);
            return ret;
        }

        private void siftDown(int idx) {
            while (true) {
                int leftChild = (idx + 1) * 2 - 1;
                int rightChild = leftChild + 1;
                int newIdx = idx;
                if (leftChild < size) {
                    if (needSift(data[leftChild], data[idx])) {
                        newIdx = leftChild;
                    }
                    if (rightChild < size) {
                        if (needSift(data[rightChild], data[idx]) && needSift(data[rightChild],
                            data[leftChild])) {
                            newIdx = rightChild;
                        }
                    }
                }
                if (newIdx != idx) {
                    T tmp = data[idx];
                    data[idx] = data[newIdx];
                    data[newIdx] = tmp;
                    idx = newIdx;
                }
                else {
                    break;
                }
            }
        }

        private boolean needSift(T child, T parent) {
            if (heapType == -1 && child.compareTo(parent) < 0
                || heapType == 1 && child.compareTo(parent) > 0) {
                return true;
            }
            return false;
        }

    }

    class Solution {

        public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
            Pair[] list = new Pair[nums1.length * nums2.length];
            int i = 0;
            for (int i1 : nums1) {
                for (int i2 : nums2) {
                    final Pair p = new Pair();
                    p.i1 = i1;
                    p.i2 = i2;
                    list[i++] = p;
                }
            }
            List<List<Integer>> ret = new ArrayList<>(k);
            final Heap<Pair> heap = new Heap<Pair>(-1, list.length).initData(list);
            for (int j = 0; j < k; j++) {
                final Pair pop = heap.pop();
                if (pop == null) {
                    break;
                }
                ret.add(new ArrayList<Integer>(2) {{
                    add(pop.i1);
                    add(pop.i2);
                }});
            }
            return ret;
        }

        public class Pair implements Comparable<Pair> {

            int i1;
            int i2;

            @Override
            public int compareTo(Pair o) {
                return this.i1 + this.i2 - (o.i1 + o.i2);
            }

        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
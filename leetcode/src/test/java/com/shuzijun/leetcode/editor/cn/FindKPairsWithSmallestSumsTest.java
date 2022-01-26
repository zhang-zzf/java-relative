//给定两个以 升序排列 的整数数组 nums1 和 nums2 , 以及一个整数 k 。 
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
//输入: nums1 = [1,7,11], nums2 = [2,4,6], k = 3
//输出: [1,2],[1,4],[1,6]
//解释: 返回序列中的前 3 对数：
//     [1,2],[1,4],[1,6],[7,2],[7,4],[11,2],[7,6],[11,4],[11,6]
// 
//
// 示例 2: 
//
// 
//输入: nums1 = [1,1,2], nums2 = [1,2,3], k = 2
//输出: [1,1],[1,1]
//解释: 返回序列中的前 2 对数：
//     [1,1],[1,1],[1,2],[2,1],[1,2],[2,2],[1,3],[1,3],[2,3]
// 
//
// 示例 3: 
//
// 
//输入: nums1 = [1,2], nums2 = [3], k = 3 
//输出: [1,3],[2,3]
//解释: 也可能序列中所有的数对都被返回:[1,3],[2,3]
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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class FindKPairsWithSmallestSumsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {

    }

    public static class Heap<T extends Comparable<T>> {

        private final HeapType heapType;
        private final int capacity;
        private int size;
        private T[] data;

        public Heap(HeapType heapType, int capacity) {
            this.heapType = heapType;
            this.capacity = capacity;
            data = (T[]) new Object[capacity];
        }

        public void initData(T[] data) {
            if (data.length > capacity) {
                throw new IllegalArgumentException();
            }
            for (T t : data) {
                data[size++] = t;
                siftUp();
            }
        }

        private void siftUp() {

        }

        public T peek() {
            return null;
        }

        public T pop() {
            return null;
        }

        public static class HeapType {

            public static final HeapType MIN = new HeapType(-1);
            public static final HeapType MAX = new HeapType(1);

            private final int type;

            public HeapType(int type) {
                this.type = type;
            }

        }

    }

    //leetcode submit region begin(Prohibit modification and deletion)
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
            for (i = 0; i < k; i++) {
                Pair topK = findTopK(list, k);
                ret.add(new ArrayList<Integer>(2) {{
                    add(topK.i1);
                    add(topK.i2);
                }});
            }
            return ret;
        }


        private Pair findTopK(Pair[] list, int k) {

            return null;
        }

        public class Pair {

            int i1;
            int i2;

        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}
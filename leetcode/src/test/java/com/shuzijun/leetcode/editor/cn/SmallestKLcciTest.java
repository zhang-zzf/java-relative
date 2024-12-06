// 设计一个算法，找出数组中最小的k个数。以任意顺序返回这k个数均可。
//
// 示例： 
//
// 输入： arr = [1,3,5,7,2,4,6,8], k = 4
// 输出： [1,2,3,4]
// 
//
// 提示： 
//
// 
// 0 <= len(arr) <= 100000 
// 0 <= k <= min(100000, len(arr)) 
// 
// Related Topics 数组 分治 快速选择 排序 堆（优先队列） 👍 178 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;
import java.util.Comparator;
import org.junit.jupiter.api.Test;


public class SmallestKLcciTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        // [1,3,5,7,2,4,6,8]
        // 4
        then(solution.smallestK(new int[]{1, 3, 5, 7, 2, 4, 6, 8}, 4)).hasSize(4);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int[] smallestK(int[] arr, int k) {
            QuickSortK algorithm = new QuickSortK();
            QuickSortK.Item<Integer, Object>[] items = new QuickSortK.Item[arr.length];
            for (int i = 0; i < arr.length; i++) {
                items[i] = algorithm.new Item<>(arr[i], null);
            }
            final QuickSortK.Item<Integer, Object>[] sortK = algorithm.sortK(items, k);
            return Arrays.stream(sortK).mapToInt(i -> i.value).toArray();
        }


        class QuickSortK {

            private final Comparator<? super Item> comparator;

            public QuickSortK(Comparator<? super Item> comparator) {
                this.comparator = comparator;
            }

            public QuickSortK() {
                this(null);
            }

            public Item[] sortK(Item[] array, int k) {
                if (array.length == 0 || k == 0) {
                    return new Item[0];
                }
                quickSortK(array, k, 0, array.length - 1);
                return Arrays.copyOf(array, k);
            }

            private void quickSortK(Item[] arr, int k, int left, int right) {
                int pivotIdx = partition(arr, left, right);
                if (pivotIdx == k - 1 || pivotIdx == k || left >= right) {
                    return;
                }
                else if (pivotIdx > k) {
                    quickSortK(arr, k, left, pivotIdx - 1);
                }
                else {
                    quickSortK(arr, k, pivotIdx + 1, right);
                }
            }

            private int partition(Item[] arr, int left, int right) {
                Item pivot = arr[right];
                int ptr = left - 1;
                for (int i = left; i < right; i++) {
                    if (this.comparator != null) {
                        if (this.comparator.compare(arr[i], pivot) < 0) {
                            swap(arr, ++ptr, i);
                        }
                    }
                    else {
                        if (arr[i].value.compareTo(pivot.value) < 0) {
                            swap(arr, ++ptr, i);
                        }
                    }
                }
                swap(arr, ptr + 1, right);
                return ptr + 1;
            }

            private void swap(Item[] arr, int i, int i1) {
                Item tmp = arr[i];
                arr[i] = arr[i1];
                arr[i1] = tmp;
            }

            class Item<V extends Comparable<V>, D> implements Comparable<Item> {

                V value;
                D extraData;

                public Item(V value, D extraData) {
                    this.value = value;
                    this.extraData = extraData;
                }

                @Override
                public int compareTo(Item o) {
                    return this.value.compareTo((V) o.value);
                }

            }

        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}
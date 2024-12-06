package com.shuzijun.leetcode.editor.cn;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author zhanfeng.zhang
 * @date 2022/02/18
 */
public class QuickSortK {

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





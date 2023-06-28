package com.feng.learn.algorithm;

/**
 * @author zhanfeng.zhang
 * @date 2020/5/2
 */
public class Algorithms {

  public static void insertSort(int[] a) {
    for (int i = 1; i < a.length; i++) {
      if (a[i] < a[i - 1]) {
        int tmp = a[i];
        int j = i - 1;
        for (; j >= 0 && tmp < a[j]; j--) {
          a[j + 1] = a[j];
        }
        a[j + 1] = tmp;
      }
    }
  }

  public static void quickSort(int[] a, int start, int end) {
    if (start < end) {
      int divide = partition(a, start, end);
      quickSort(a, start, divide - 1);
      quickSort(a, divide + 1, end);
    }
  }

  private static int partition(int[] a, int low, int high) {
    int pivot = a[high];
    // Invariant: a[idx...high] >= pivot
    int idx = low;
    for (int i = low; i < high; i++) {
      if (a[i] < pivot) {
        swap(a, idx, i);
        idx += 1;
      }
    }
    swap(a, idx, high);
    return idx;
  }

  private static void swap(int[] a, int i, int ptm) {
    if (i == ptm) {
      return;
    }
    int tmp = a[i];
    a[i] = a[ptm];
    a[ptm] = tmp;
  }

}

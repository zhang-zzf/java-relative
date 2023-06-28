package com.shuzijun.leetcode.editor.cn;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * @author zhanfeng.zhang
 * @date 2022/04/30
 */
public class Utils {

  /**
   * int[][] 2维数组字符串转数组
   *
   * @param str data
   * @return int[][]
   */
  public static int[][] to2Array(String str) {
    return JSON.parseArray(str).stream()
        .map(o -> ((JSONArray) o).stream()
            .map(io -> ((Integer) io))
            .mapToInt(Integer::intValue)
            .toArray()
        ).toArray(int[][]::new);
  }

}

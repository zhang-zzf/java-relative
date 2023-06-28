package com.github.learn.leetcode.domain.service.dp;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/27
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class MaxProfitServiceDpImpl implements MaxProfitService {

  @Override
  public int maxProfit(@NotNull List<Task> taskList) {
    int size = taskList.size();
    int[] maxProfit = new int[size + 1];
    maxProfit[0] = 0;
    for (int i = 0; i < size; i++) {
      int p1 = taskList.get(i).getValue() + maxProfit[prev(taskList, i) + 1];
      int p2 = maxProfit[i];
      maxProfit[i + 1] = Math.max(p1, p2);
    }
    return maxProfit[size];
  }

  private int prev(List<Task> taskList, int i) {
    for (int j = i - 1; j >= 0; j--) {
      if (taskList.get(j).getEnd() <= taskList.get(i).getStart()) {
        return j;
      }
    }
    return -1;
  }

}

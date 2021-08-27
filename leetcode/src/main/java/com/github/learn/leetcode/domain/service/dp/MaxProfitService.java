package com.github.learn.leetcode.domain.service.dp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/27
 */
public interface MaxProfitService {


    int maxProfit(@NotNull List<Task> taskList);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Task {
        int start;
        int end;
        int value;
    }
}

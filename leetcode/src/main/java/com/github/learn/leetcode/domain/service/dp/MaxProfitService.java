package com.github.learn.leetcode.domain.service.dp;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

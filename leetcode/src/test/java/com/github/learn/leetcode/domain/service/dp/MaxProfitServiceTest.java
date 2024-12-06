package com.github.learn.leetcode.domain.service.dp;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/27
 */
@SpringBootTest
class MaxProfitServiceTest {

    @Autowired
    @Qualifier("maxProfitServiceDpImpl")
    MaxProfitService service;

    @Test
    void given_when_then() {
        final List<MaxProfitService.Task> taskList = new ArrayList<MaxProfitService.Task>(8) {{
            add(new MaxProfitService.Task(1, 4, 5));
            add(new MaxProfitService.Task(3, 5, 1));
            add(new MaxProfitService.Task(0, 6, 8));
            add(new MaxProfitService.Task(4, 7, 4));
            add(new MaxProfitService.Task(3, 8, 6));
            add(new MaxProfitService.Task(5, 9, 3));
            add(new MaxProfitService.Task(6, 10, 2));
            add(new MaxProfitService.Task(8, 11, 4));
        }};
        then(service.maxProfit(taskList.subList(0, 1))).isEqualTo(5);
        then(service.maxProfit(taskList.subList(0, 3))).isEqualTo(8);
        then(service.maxProfit(taskList)).isEqualTo(13);
    }

}
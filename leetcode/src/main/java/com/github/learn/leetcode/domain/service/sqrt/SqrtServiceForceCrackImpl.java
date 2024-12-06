package com.github.learn.leetcode.domain.service.sqrt;

import javax.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/15
 */
@Service
@Validated
@Slf4j
public class SqrtServiceForceCrackImpl implements SqrtService {

    /**
     * <p>开根号</p>
     * <p>实现语言不限，你条件可以比上述更加苛刻，但不能宽松。例如调用你的接口 sqrt(9, 0.21) 返回值属于 [2.79, 3.21] 这个区间的任意一个都满足条件。</p>
     *
     * @param v 参数
     * @param t 误差
     * @return 平方根
     */
    @Override
    public @PositiveOrZero double sqrt(@PositiveOrZero int v, @PositiveOrZero double t) {
        int cnt = 0;
        for (int i = 0; i < v; i++) {
            if (i * i > v + t) {
                for (double j = i - 1; j < i; j += t) {
                    if (j * j > v - t) {
                        log.info("cnt: {}", cnt);
                        return j;
                    }
                    cnt += 1;
                }
            }
            cnt += 1;
        }
        return 0;
    }

}

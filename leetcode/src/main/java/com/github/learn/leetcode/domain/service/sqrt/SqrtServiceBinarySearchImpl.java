package com.github.learn.leetcode.domain.service.sqrt;

import javax.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/15
 */
@Validated
@Service
@Primary
@Slf4j
public class SqrtServiceBinarySearchImpl implements SqrtService {

    @Override
    public @PositiveOrZero double sqrt(@PositiveOrZero int v, @PositiveOrZero double t) {
        double left = 0, right = v;
        int cnt = 0;
        while (true) {
            double mid = (left + right) / 2;
            final double tmp = mid * mid;
            if (tmp > v + t) {
                right = mid - t;
                cnt += 1;
            }
            else if (tmp < v - t) {
                left = mid + t;
                cnt += 1;
            }
            else {
                log.info("cnt: {}", cnt);
                return mid;
            }
        }
    }
}

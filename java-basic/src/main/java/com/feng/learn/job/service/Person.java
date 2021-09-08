package com.feng.learn.job.service;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/05
 */
@Data
@Accessors(chain = true)
public class Person {

    private JobPosition currentJobPosition;
}

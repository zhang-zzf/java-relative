package com.github.learn.spring.event;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/14
 */
@Data
@Accessors(chain = true)
public class Event {
    private Long id;

}

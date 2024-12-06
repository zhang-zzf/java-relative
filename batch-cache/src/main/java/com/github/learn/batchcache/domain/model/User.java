package com.github.learn.batchcache.domain.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhanfeng.zhang
 * @date 2020/7/24
 */
@Data
@Accessors(chain = true)
public class User {

    private Long id;
    private String name;
}

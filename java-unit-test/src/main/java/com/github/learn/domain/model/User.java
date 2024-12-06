package com.github.learn.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/04
 */
@Data
@Accessors(chain = true)
@Builder
public class User {

    private Long id;
    private String name;
}

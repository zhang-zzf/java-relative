package com.github.learn.mapstruct_demo.object.json;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhanfeng.zhang
 * @date 2021/11/16
 */
@Data
@Accessors(chain = true)
public class Dto {

    private String str;

    /**
     * json
     */
    private String person;

    /**
     * json
     */
    private String mapData;

}

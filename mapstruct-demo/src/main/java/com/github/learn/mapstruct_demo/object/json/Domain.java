package com.github.learn.mapstruct_demo.object.json;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author zhanfeng.zhang
 * @date 2021/11/16
 */
@Data
@Accessors(chain = true)
public class Domain {

    private String str;
    private Person person;
    private Map<String, Object> mapData;

}

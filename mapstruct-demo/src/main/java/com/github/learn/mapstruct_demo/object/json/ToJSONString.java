package com.github.learn.mapstruct_demo.object.json;

import com.alibaba.fastjson.JSON;
import org.mapstruct.Named;

/**
 * @author zhanfeng.zhang
 * @date 2021/11/17
 */
public class ToJSONString {


  @Named("toJSONStr")
  public String toJSONString(Object o) {
    return JSON.toJSONString(o);
  }


}

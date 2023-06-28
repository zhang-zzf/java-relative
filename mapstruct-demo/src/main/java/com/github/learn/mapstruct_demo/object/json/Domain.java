package com.github.learn.mapstruct_demo.object.json;

import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

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

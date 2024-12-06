package com.github.learn.mapstruct_demo.object.collection.diff_item;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhanfeng.zhang
 * @date 2021/11/16
 */
@Data
@Accessors(chain = true)
public class Dto {

    private List<PersonDto> personList;

}

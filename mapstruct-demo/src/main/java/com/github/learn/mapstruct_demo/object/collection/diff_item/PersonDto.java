package com.github.learn.mapstruct_demo.object.collection.diff_item;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PersonDto {

  private String name;
  private int age;

}

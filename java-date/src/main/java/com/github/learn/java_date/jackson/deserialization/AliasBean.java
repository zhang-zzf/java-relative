package com.github.learn.java_date.jackson.deserialization;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class AliasBean {

    @JsonAlias({"_first_name", "fName", "f_name"})
    String firstName;
    String lastName;

}

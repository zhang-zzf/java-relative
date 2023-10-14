package com.github.learn.jackson;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UnwrappedUser {

    Long id;
    @JsonUnwrapped(suffix = "Name")
    Name name;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Name {

        String first;
        String last;

    }

}

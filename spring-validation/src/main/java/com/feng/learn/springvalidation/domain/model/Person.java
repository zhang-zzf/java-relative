package com.feng.learn.springvalidation.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/10
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Person {

    @NotNull
    @Positive
    private Long id;
    private String name;
    @NotNull
    @Valid
    private Address address;

    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    public static class Address {

        @NotBlank
        private String province;
        private String city;
        private String area;

    }

}

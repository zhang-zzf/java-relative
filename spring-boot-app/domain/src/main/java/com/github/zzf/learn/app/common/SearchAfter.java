package com.github.zzf.learn.app.common;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.Data;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-03-13
 */
@Data
public class SearchAfter {

    @NotNull
    Long id;
    @NotNull
    @Max(2000)
    Integer size;

    @Nullable
    @Valid
    Between<Date> updatedAt;

    @Data
    public static class Between<T> {
        @NotNull
        T gte;
        T lte;
    }

}

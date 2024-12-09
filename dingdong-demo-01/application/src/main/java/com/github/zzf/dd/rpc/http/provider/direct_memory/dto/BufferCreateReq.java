package com.github.zzf.dd.rpc.http.provider.direct_memory.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BufferCreateReq {
    @NotNull
    @Min(1L)
    Integer count;
    @NotNull
    @Min(1L)
    Integer capacity;
}

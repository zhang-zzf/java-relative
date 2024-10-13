package com.github.zzf.actuator.rpc.http.provider.direct_memory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BufferCreateReq {
    @NotNull @Min(1L) Integer count;
    @NotNull @Min(1L) Integer capacity;
}

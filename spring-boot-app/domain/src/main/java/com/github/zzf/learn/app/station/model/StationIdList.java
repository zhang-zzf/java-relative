package com.github.zzf.learn.app.station.model;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-03-08
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationIdList {
    Set<Long> idSet;
}

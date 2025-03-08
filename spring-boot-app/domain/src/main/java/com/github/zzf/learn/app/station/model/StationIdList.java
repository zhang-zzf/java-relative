package com.github.zzf.learn.app.station.model;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-03-08
 */
@Getter
@Builder
public class StationIdList {
    Set<Long> idSet;
}

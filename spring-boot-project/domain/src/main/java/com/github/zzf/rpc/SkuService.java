package com.github.zzf.rpc;

import com.github.zzf.actuator.entity.Sku;

public interface SkuService {
  Sku queryById(Long skuId);
}

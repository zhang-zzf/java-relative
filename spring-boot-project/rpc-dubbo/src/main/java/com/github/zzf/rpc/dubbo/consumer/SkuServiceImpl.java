package com.github.zzf.rpc.dubbo.consumer;

import com.github.zzf.rpc.SkuService;
import com.github.zzf.entity.Sku;
import org.springframework.stereotype.Service;

@Service
public class SkuServiceImpl implements SkuService {
  @Override
  public Sku queryById(Long skuId) {
    return null;
  }
}

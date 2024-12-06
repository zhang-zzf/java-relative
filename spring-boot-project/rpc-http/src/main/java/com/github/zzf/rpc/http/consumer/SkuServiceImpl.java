package com.github.zzf.rpc.http.consumer;

import com.github.zzf.actuator.entity.Sku;
import com.github.zzf.rpc.SkuService;
import org.springframework.stereotype.Service;

@Service
public class SkuServiceImpl implements SkuService {
    @Override
    public Sku queryById(Long skuId) {
        return new Sku().setName("rpc-http");
    }
}

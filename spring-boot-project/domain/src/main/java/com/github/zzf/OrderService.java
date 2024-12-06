package com.github.zzf;

import com.github.zzf.actuator.entity.Order;
import com.github.zzf.actuator.entity.Sku;
import com.github.zzf.rpc.SkuService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 订单服务
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    final SkuService skuService;

    public Order queryById(Long id) {
        Sku sku = skuService.queryById(1L);
        return new Order().setSkuList(List.of(sku));
    }
}

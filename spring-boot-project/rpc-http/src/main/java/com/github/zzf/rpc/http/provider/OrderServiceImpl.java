package com.github.zzf.rpc.http.provider;

import com.github.zzf.OrderService;
import com.github.zzf.actuator.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderServiceImpl {

  final OrderService orderService;

  @GetMapping("/order/{id}")
  public Order getByOrderId(@PathVariable Long id) {
    return orderService.queryById(id);
  }
}

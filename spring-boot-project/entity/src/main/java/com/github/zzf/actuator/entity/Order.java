package com.github.zzf.actuator.entity;

import java.util.List;
import lombok.Data;

@Data
public class Order {
  String orderId;
  List<Sku> skuList;
}

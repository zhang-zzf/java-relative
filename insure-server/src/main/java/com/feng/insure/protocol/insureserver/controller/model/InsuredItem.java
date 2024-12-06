package com.feng.insure.protocol.insureserver.controller.model;

import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 被保 Item
 * <p>保险标的</p>
 *
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/9/23
 */
@Data
@Accessors(chain = true)
public class InsuredItem {

    private Map<String, String> data;

}

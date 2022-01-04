package com.feng.insure.protocol.insureserver.controller.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/12/7
 */
@Data
@Accessors(chain = true)
public class ErrorData {

    private String path;
    private String status;
    private String code;
    private String message;

}

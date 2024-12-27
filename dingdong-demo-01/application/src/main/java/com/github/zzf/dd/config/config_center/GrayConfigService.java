package com.github.zzf.dd.config.config_center;

import org.springframework.stereotype.Service;

/**
 * 配置中心，灰度服务
 */
@Service
public class GrayConfigService {
    public boolean hitUserListGray() {
        return true;
    }
}

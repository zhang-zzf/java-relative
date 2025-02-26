package com.github.learn.springframework.resource_annotation;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-02-26
 */
@Service
public class AService {

    @Resource
    private BService bService;

    public String bServiceName() {
        return bService.name();
    }
}

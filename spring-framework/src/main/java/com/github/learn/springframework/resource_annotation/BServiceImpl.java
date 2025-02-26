package com.github.learn.springframework.resource_annotation;

import org.springframework.stereotype.Service;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-02-26
 */
@Service
public class BServiceImpl implements BService{
    @Override
    public String name() {
        return "bServiceImpl";
    }
}

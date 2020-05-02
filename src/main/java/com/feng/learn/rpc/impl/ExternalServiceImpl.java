package com.feng.learn.rpc.impl;

import com.feng.learn.rpc.ExternalService;
import org.springframework.stereotype.Service;

@Service("externalService")
public class ExternalServiceImpl implements ExternalService {

    @Override
    public String service(long l) {
        return String.valueOf(l);
    }
}

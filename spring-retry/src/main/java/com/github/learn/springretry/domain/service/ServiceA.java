package com.github.learn.springretry.domain.service;

import com.github.learn.springretry.infra.facade.FacadeA;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/14
 */
@Service
@RequiredArgsConstructor
public class ServiceA {

    final FacadeA facadeA;

    public void methodA() {
        facadeA.methodA();
    }

}

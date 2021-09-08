package com.feng.learn.lombok;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2020/06/05
 */
@Data
@Slf4j
@RequiredArgsConstructor
public class LombokInnerClass {

    @Data
    static class StaticInnerClass {
        private String name;

    }

    @Data
    class InnerClass {
        private String name;
    }
}

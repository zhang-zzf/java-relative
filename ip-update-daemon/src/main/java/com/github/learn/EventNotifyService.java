package com.github.learn;

import java.util.Set;

/**
 * @author zhanfeng.zhang
 * @date 2022/04/06
 */
public interface EventNotifyService {

    /**
     * 发送消息
     *
     * @param hostName 主机
     * @param curIpSet Ip
     */
    void sendIpChangeEvent(String hostName, Set<String> curIpSet);

}

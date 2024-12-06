package com.github.learn;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2022/04/05
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class IpChangeListener {

    private final IpQueryService ipQueryService;
    private final List<EventNotifyService> eventNotifyService;

    @SneakyThrows
    @PostConstruct
    public void start() {
        Runnable task = () -> {
            try {
                Set<String> curIpSet = new HashSet<>();
                while (true) {
                    final Set<String> allPublicIp = ipQueryService.queryAllPublicIp();
                    if (!curIpSet.equals(allPublicIp)) {
                        // 公网地址发生变更
                        curIpSet = allPublicIp;
                        for (EventNotifyService notifyService : eventNotifyService) {
                            notifyService.sendIpChangeEvent(InetAddress.getLocalHost().getHostName(), curIpSet);
                        }
                    }
                    // sleep 1000 ms
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                log.error("unExpected Exception ", e);
            }
        };
        // 启动线程
        new Thread(task).start();
    }


}

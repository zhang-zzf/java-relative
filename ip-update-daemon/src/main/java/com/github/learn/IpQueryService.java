package com.github.learn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhanfeng.zhang
 * @date 2022/04/05
 */
@Slf4j
@Service
public class IpQueryService {

    public Set<String> queryAllPublicIp() throws UnknownHostException, SocketException {
        Set<String> publicIPSet = new HashSet<>(8);
        final InetAddress localHost = InetAddress.getLocalHost();
        final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            final NetworkInterface ni = networkInterfaces.nextElement();
            final Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
            final String nicName = ni.getDisplayName();
            while (inetAddresses.hasMoreElements()) {
                final InetAddress inetAddress = inetAddresses.nextElement();
                final boolean loopbackAddress = inetAddress.isLoopbackAddress();
                final boolean anyLocalAddress = inetAddress.isAnyLocalAddress();
                final boolean linkLocalAddress = inetAddress.isLinkLocalAddress();
                final boolean siteLocalAddress = inetAddress.isSiteLocalAddress();
                final String hostAddress = inetAddress.getHostAddress();
                if (loopbackAddress || anyLocalAddress || linkLocalAddress || siteLocalAddress) {
                    continue;
                }
                log.info("hostName: {}, nicName: {}, hostAddress: {}", localHost.getHostName(), nicName, hostAddress);
                publicIPSet.add(hostAddress);
            }
        }
        return publicIPSet;
    }

}

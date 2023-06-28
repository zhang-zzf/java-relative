package com.feng.learn.rate.limit;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/24
 */
public class RequestLimit {

  private final long threshold;
  private final int periodInMills;
  private volatile Map<String, RequestCounter> url2Counter = new HashMap<>();

  public RequestLimit(long threshold, int periodInMills) {
    this.threshold = threshold;
    this.periodInMills = periodInMills;
  }

  /**
   * 请求限流器
   *
   * @param url 请求url
   * @return true - 不限流；false - 限流
   */
  public boolean rateLimit(String url) {
    RequestCounter c = url2Counter.get(url);
    if (c == null) {
      synchronized (this) {
        Map<String, RequestCounter> newMap = new HashMap<>(url2Counter);
        newMap.put(url, new RequestCounter(periodInMills));
        url2Counter = newMap;
      }
      c = url2Counter.get(url);
    }
    if (c.add(1) > threshold) {
      return false;
    }
    return true;
  }

}

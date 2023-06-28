package com.github.learn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2022/04/05
 */
@Slf4j
@Service
public class EventNotifyServiceDingDingImpl implements EventNotifyService {

  private final String dingDingWebhook;

  public EventNotifyServiceDingDingImpl(
      @Value("${dingding.webhook:#{null}}") String dingDingWebhook) {
    this.dingDingWebhook = dingDingWebhook;
  }

  @Override
  @SneakyThrows
  public void sendIpChangeEvent(String hostName, Set<String> curIpSet) {
    if (dingDingWebhook == null) {
      return;
    }
    String msgContent = hostName + " IP: " + curIpSet;
    /**
     * {
     *     "at": {
     *         "atMobiles":[
     *             "180xxxxxx"
     *         ],
     *         "atUserIds":[
     *             "user123"
     *         ],
     *         "isAtAll": false
     *     },
     *     "text": {
     *         "content":"我就是我, @XXX 是不一样的烟火"
     *     },
     *     "msgtype":"text"
     * }
     */
    final StringBuilder buf = new StringBuilder();
    buf.append('{');
    buf.append("\"msgtype\":\"text\"").append(',');
    buf.append("\"text\": {\"content\":\"").append(msgContent).append("\"}");
    buf.append('}');
    log.info("req: {}", buf.toString());
    HttpURLConnection con = (HttpURLConnection) new URL(dingDingWebhook).openConnection();
    try {
      con.setRequestMethod("POST");
      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("Accept", "application/json");
      con.setConnectTimeout(5000);
      con.setReadTimeout(5000);
      con.setDoOutput(true);
      try (OutputStream os = con.getOutputStream()) {
        byte[] input = buf.toString().getBytes("utf-8");
        os.write(input, 0, input.length);
      }
      try (BufferedReader br = new BufferedReader(
          new InputStreamReader(con.getInputStream(), "utf-8"))) {
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
          response.append(responseLine.trim());
        }
        log.info("resp: {}", response.toString());
      }
    } finally {
      con.disconnect();
    }
  }

}

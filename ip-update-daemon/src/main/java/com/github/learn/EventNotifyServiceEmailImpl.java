package com.github.learn;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2022/04/05
 */
@Slf4j
@Service
public class EventNotifyServiceEmailImpl implements EventNotifyService {

  private final MailSender mailSender;
  private final String sender;
  private final String receiverList;

  public EventNotifyServiceEmailImpl(@Value("${email.sender.host:#{null}}") String host,
      @Value("${email.sender.username:#{null}}") String sender,
      @Value("${email.sender.password:#{null}}") String password,
      @Value("${email.receivers:#{null}}") String receiverList) {
    this.mailSender = mailSender(host, sender, password);
    this.sender = sender;
    this.receiverList = receiverList;
  }

  @Override
  public void sendIpChangeEvent(String hostName, Set<String> curIpSet) {
    if (sender == null) {
      // 没有配置
      return;
    }
    final SimpleMailMessage m = new SimpleMailMessage();
    m.setFrom(sender);
    m.setTo(receiverList.split(","));
    m.setSubject("Ip change event: " + hostName);
    m.setText("Public IP set: " + curIpSet);
    mailSender.send(m);
  }

  public MailSender mailSender(String host, String username, String password) {
    final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(host);
    mailSender.setUsername(username);
    mailSender.setPassword(password);
    return mailSender;
  }


}

package com.github.learn.springweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanfeng.zhang
 * @date 2022/06/15
 */
@Controller
@RestController
@Slf4j
public class StringBodyPostController {

  @PostMapping("/api_1")
  public Object api_1(@RequestHeader("Access-Token") String token, @RequestBody String body) {
    log.info("post request:{}, {}", token, body);
    return body;
  }

}

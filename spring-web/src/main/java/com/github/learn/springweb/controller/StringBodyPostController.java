package com.github.learn.springweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 以 String 接收 POST 的 Body
 */
@Controller
@RestController
@Slf4j
@RequestMapping("/")
public class StringBodyPostController {


    @PostMapping("/api_1")
    public Object api_1(
        @RequestHeader(name = "Access-Token", required = false) String token,
        @RequestBody(required = false) String body) {
        log.info("post request:{}, {}", token, body);
        return body;
    }

    @PostMapping("/api_2")
    public Object api_2(
        @RequestHeader(name = "Access-Token", required = false) String token,
        @RequestBody(required = false) byte[] body) {
        log.info("post request:{}, {}", token, body);
        return body;
    }


}

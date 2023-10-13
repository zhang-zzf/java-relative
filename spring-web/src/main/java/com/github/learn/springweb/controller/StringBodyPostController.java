package com.github.learn.springweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

}

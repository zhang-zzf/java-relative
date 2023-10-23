package com.github.learn.springweb.controller;

import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/")
public class ControllerCases {

  /**
   * <pre>
   *   todo
   *   - Query http method
   *   - Query http path
   *   - Query http path params
   *   - Query http header
   *   - Query http Body
   *      - JSON body as String [done]
   *      - form / file
   *   - POST url body 参数覆盖
   *   - Map<String,String> 接受 parameters(参考 @RequestParam) [done]
   *   - GET 请求可否携带 body
   *
   * </pre>
   *
   * @return
   */
  public Object api_2() {
    return new Object();
  }

  /**
   * GET / POST / PUT / DELETE / HEAD / OPTION 都可以使用
   */
  @RequestMapping("/greeting")
  public Greeting greeting(
      @RequestParam("id") Long id,
      @RequestParam("content") String content) {
    return new Greeting().setId(id).setContent(content);
  }

  /**
   * 实测：GET 可以携带 body 可以，但不推荐
   */
  @GetMapping("/greeting")
  public Greeting getGreeting(@RequestBody Greeting greeting) {
    return greeting;
  }

  /**
   * <pre>
   *    获取 POST body 中的 json 字段参数
   *    You can use the @RequestBody annotation to have the request body read and deserialized into an Object through an HttpMessageConverter
   *    所以，@RequestBody 设计时是把 post body 中的 json 整体映射到一个 Model Object。
   *    没有办法绑定 json 中的字段到参数中
   *    本方法会 400
   * </pre>
   */
  @PostMapping("/greeting/post")
  public Greeting postGreeting(
      @RequestBody Long id,
      @RequestBody String content) {
    return new Greeting().setId(id).setContent(content);
  }

  /**
   * @param user_id
   * @param user_name  CookieValue 接受 Cookie 参数
   * @param pathMap    获取 Path 变量
   * @param headers    获取 请求头
   * @param parameters 获取 url 中的参数
   * @param body       获取 POST body 中的参数
   */
  @PostMapping("/greeting/post/map/{id}")
  public Greeting postGreetingMap(
      @CookieValue(name = "user_id", required = false) Long user_id,
      @CookieValue(name = "user_name", required = false) String user_name,
      @PathVariable Map<String, String> pathMap,
      @RequestHeader Map<String, String> headers,
      @RequestParam Map<String, String> parameters,
      @RequestBody Map<String, Object> body) {
    return new Greeting()
        .setId((Long) body.get("id"))
        .setContent((String) body.get("content"));
  }


  @Data
  public static class Greeting {
    Long id;
    String content;
  }

}

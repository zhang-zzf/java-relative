package com.github.learn.infra.http.feign;

import com.github.learn.infra.http.dto.HttpMethodPostBody;
import com.github.learn.infra.http.dto.HttpMethodsResp;
import feign.QueryMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(path = "/", headers = {"Content-Type: application/xml"})
public interface HttpBinWithSpring4 {

  @GetMapping(value = "/get",
      headers = {"Content-Type: application/json",/* 同名覆盖 */})
  HttpMethodsResp httpMethodGet(@RequestHeader("X-User-Id") String userId);

  @GetMapping("/base64/{val}")
  String dynamicDataBase64(@PathVariable String val);

  /**
   * @param map 添加 QueryMap 参数，序列化到 url 的请求参数中
   *            POST https://httpbin.org/post?map=name=%E5%BC%A0%E5%8D%A0%E5%B3%B0&map=id=1 HTTP/1.1
   *            map 中的多个参数并未按照key=value 的形式被映射到 url 参数中
   *            建议多个参数，写多个 @RequestParam。
   *
   * @param body  请求 body
   */
  @PostMapping(value = "/post",
      headers = {"Content-Type: application/json",/* 同名覆盖 */})
  HttpMethodsResp httpMethodPostWithParamAndBody(
      @RequestHeader("X-User-Id") String userId,
      // @RequestParam HttpMethodsGetParam queries,
      // 当需要把多个参数映射到 query 上时只能使用 Map<String, String>
      // POST https://httpbin.org/post?map=name=%E5%BC%A0%E5%8D%A0%E5%B3%B0&map=id=1 HTTP/1.1
      @RequestParam Map<String, String> map,
      @RequestBody HttpMethodPostBody body);
}


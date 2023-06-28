package com.feng.insure.protocol.insureserver.controller;

import com.feng.insure.protocol.insureserver.controller.model.ErrorData;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/12/8
 */
@ControllerAdvice
public class GlobalControllerAdvice {

  @ResponseBody
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorData illegalArgumentHandler(IllegalArgumentException ex, HttpServletRequest request) {
    final ErrorData resp = new ErrorData().setPath(request.getServletPath())
        .setStatus(HttpStatus.BAD_REQUEST.name())
        .setCode("INVALID_ARGUMENT")
        .setMessage(ex.getMessage());
    return resp;
  }


  @ResponseBody
  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String notFoundHandler(NotFoundException ex) {
    return ex.getMessage();
  }

  public static class NotFoundException extends RuntimeException {

  }


}

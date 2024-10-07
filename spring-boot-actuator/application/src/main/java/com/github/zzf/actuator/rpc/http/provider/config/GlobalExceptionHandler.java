package com.github.zzf.actuator.rpc.http.provider.config;

import com.github.zzf.actuator.common.exception.BusinessException;
import com.github.zzf.actuator.common.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler implements InitializingBean {

    private MessageSource messageSource;

    /**
     * 参数校验非法，抛 IllegalArgumentException
     */
    @ExceptionHandler({IllegalArgumentException.class, ConstraintViolationException.class})
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(Exception e) {
        log.error("unexpected Exception", e);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ProblemDetail body = problemDetail(httpStatus, e);
        return new ResponseEntity<>(body, HttpHeaders.EMPTY, httpStatus);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(RuntimeException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ProblemDetail body = problemDetail(httpStatus, e);
        return new ResponseEntity<>(body, HttpHeaders.EMPTY, httpStatus);
    }

    // @ExceptionHandler({RuntimeException.class, BusinessException.class})
    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<ProblemDetail> handleRuntimeException(RuntimeException e) {
        log.error("unexpected RuntimeException", e);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ProblemDetail body = problemDetail(httpStatus, e);
        return new ResponseEntity<>(body, HttpHeaders.EMPTY, httpStatus);
    }

    private static String toExceptionCode(Exception e) {
        if (e.getMessage() == null) {
            return e.getClass().getName() + "." + e.getMessage();
        }
        return e.getClass().getName() + "." + e.getMessage().replaceAll(" ", "_");
    }

    private ProblemDetail problemDetail(HttpStatus status, Exception e) {
        ProblemDetail ret = ProblemDetail.forStatus(status);
        Object[] args = null;
        if (e instanceof BusinessException be) {
            args = be.getMessageDetailArguments();
        }
        String exceptionCode = toExceptionCode(e);
        String message = messageSource.getMessage(exceptionCode, args, LocaleContextHolder.getLocale());
        ret.setDetail(exceptionCode);
        ret.setProperty("timestamp", LocalDateTime.now());
        ret.setProperty("error", status.getReasonPhrase());
        ret.setProperty("message", message);
        return ret;
    }

    @Override
    public void afterPropertiesSet() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("http/i18n/Exceptions");
        ms.setUseCodeAsDefaultMessage(true);
        this.messageSource = ms;
    }
}

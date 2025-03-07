package com.github.zzf.learn.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final Object[] messageDetailArguments;

    public BusinessException() {
        this(null);
    }

    public BusinessException(String message) {
        this(message, new Object[]{});
    }

    public BusinessException(String message, Object... messageDetailArguments) {
        this(message, null, messageDetailArguments);
    }

    public BusinessException(String message, Throwable cause) {
        this(message, cause, new Object[0]);
    }

    public BusinessException(String message, Throwable cause, Object... messageDetailArguments) {
        super(message, cause);
        this.messageDetailArguments = messageDetailArguments;
    }

}

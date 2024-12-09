package com.github.zzf.dd.common.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException() {
        this(null);
    }

    public ResourceNotFoundException(String message) {
        this(message, new Object[]{});
    }

    public ResourceNotFoundException(String message, Object... messageDetailArguments) {
        this(message, null, messageDetailArguments);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        this(message, cause, new Object[0]);
    }

    public ResourceNotFoundException(String message, Throwable cause, Object... messageDetailArguments) {
        super(message, cause, messageDetailArguments);
    }

}

package com.microsoft.ganesha.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomException extends RuntimeException {
    public String correlationId;
    public String message;

    public CustomException(String message, String correlationId) {
        super(message);
        this.correlationId = correlationId;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

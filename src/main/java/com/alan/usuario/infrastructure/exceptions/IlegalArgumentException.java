package com.alan.usuario.infrastructure.exceptions;

public class IlegalArgumentException extends RuntimeException {
    public IlegalArgumentException(String message) {
        super(message);
    }

    public IlegalArgumentException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

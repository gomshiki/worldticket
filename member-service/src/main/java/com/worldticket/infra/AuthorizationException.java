package com.worldticket.infra;

public class AuthorizationException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "유효하지 않은 토큰입니다.";

    public AuthorizationException() {
        super(DEFAULT_MESSAGE);
    }

    public AuthorizationException(String message) {
        super(message);
    }
}

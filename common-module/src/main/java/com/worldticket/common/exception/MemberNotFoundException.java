package com.worldticket.common.exception;

public class MemberNotFoundException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "해당하는 회원 정보가 없습니다.";

    public MemberNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}

package com.worldticket.infra;

public class DataNotFoundException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "해당하는 데이터가 없습니다.";

    public DataNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public DataNotFoundException(String message) {
        super(message);
    }
}

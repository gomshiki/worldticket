package com.worldticket.fifo.globalutilities.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {
    ACCESS_TOKEN("Authorization"),
    REFRESH_TOKEN("Refresh-Token");

    private final String header;
}

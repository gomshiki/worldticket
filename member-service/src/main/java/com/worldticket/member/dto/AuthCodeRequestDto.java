package com.worldticket.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthCodeRequestDto {
    private String email;
    private String code;
}

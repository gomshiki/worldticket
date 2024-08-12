package com.worldticket.fifo.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponseDto {
    private Long memberId;
    private String memberName;
    private String email;
}

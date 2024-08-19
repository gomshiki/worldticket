package com.worldticket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberRequestDto {
    private Long memberId;
    private String email;
    private String memberName;
}

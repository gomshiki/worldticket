package com.worldticket.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberRequestDto {
    private UUID memberId;
    private String email;
    private String memberName;
}

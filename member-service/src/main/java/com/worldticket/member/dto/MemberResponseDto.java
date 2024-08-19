package com.worldticket.member.dto;

import com.worldticket.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponseDto {
    private UUID memberNumber;
    private String memberName;
    private String phoneNumber;
    private String address1;
    private String address2;
    private String email;
    private LocalDateTime createAt;

    public static MemberResponseDto from(Member savedMember) {
        return MemberResponseDto.builder()
                .email(savedMember.getEmail())
                .memberName(savedMember.getMemberName())
                .memberNumber(savedMember.getMemberNumber())
                .address1(savedMember.getAddress1())
                .address2(savedMember.getAddress2())
                .createAt(savedMember.getCreateAt())
                .phoneNumber(savedMember.getPhoneNumber())
                .build();
    }
}

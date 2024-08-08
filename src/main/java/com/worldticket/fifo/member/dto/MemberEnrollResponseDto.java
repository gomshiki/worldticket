package com.worldticket.fifo.member.dto;

import com.worldticket.fifo.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEnrollResponseDto {

    private Long memberId;
    private String memberName;
    private String phoneNumber;
    private String address1;
    private String address2;
    private String email;

    public static MemberEnrollResponseDto from(Member savedMember) {
        return MemberEnrollResponseDto.builder()
                .memberId(savedMember.getMemberId())
                .memberName(savedMember.getMemberName())
                .email(savedMember.getEmail())
                .address1(savedMember.getAddress1())
                .address2(savedMember.getAddress2())
                .build();
    }
}

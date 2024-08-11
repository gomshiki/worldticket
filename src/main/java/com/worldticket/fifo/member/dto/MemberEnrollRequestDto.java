package com.worldticket.fifo.member.dto;

import com.worldticket.fifo.member.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberEnrollRequestDto {

    @NotBlank(message = "이름을 입력해주세요")
    @Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하로 입력해주세요")
    private String memberName;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "유효한 이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "시,도 주소를 입력해주세요")
    private String address1;

    @NotBlank(message = "상세 주소를 입력해주세요")
    private String address2;

    @NotBlank(message = "전화번호를 입력해주세요")
    @Size(min = 10, max = 15)
    private String phoneNumber;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String password;

    public static Member from(MemberEnrollRequestDto memberEnrollRequestDto) {
        return Member.builder()
                .memberName(memberEnrollRequestDto.getMemberName())
                .password(memberEnrollRequestDto.getPassword())
                .address1(memberEnrollRequestDto.getAddress1())
                .address2(memberEnrollRequestDto.getAddress2())
                .email(memberEnrollRequestDto.getEmail())
                .phoneNumber(memberEnrollRequestDto.getPhoneNumber())
                .build();
    }


}

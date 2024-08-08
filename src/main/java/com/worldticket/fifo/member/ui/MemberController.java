package com.worldticket.fifo.member.ui;

import com.worldticket.fifo.member.application.MemberService;
import com.worldticket.fifo.member.dto.MemberEnrollRequestDto;
import com.worldticket.fifo.member.dto.MemberEnrollResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/enroll")
    public ResponseEntity<MemberEnrollResponseDto> enrollMember(@Valid @RequestBody MemberEnrollRequestDto memberEnrollRequestDto) {
        System.out.println(memberEnrollRequestDto.toString());
        return ResponseEntity.ok(memberService.enrollMember(memberEnrollRequestDto));
    }

    // 이메일 인증
    @GetMapping("/auth")
    public ResponseEntity<?> authEmail(String email) {
        return null;
    }
}

package com.worldticket.fifo.member.ui;

import com.worldticket.fifo.member.application.MemberService;
import com.worldticket.fifo.member.dto.LoginRequestDto;
import com.worldticket.fifo.member.dto.MemberEnrollRequestDto;
import com.worldticket.fifo.member.infra.MemberResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollMember(@Valid @RequestBody MemberEnrollRequestDto memberEnrollRequestDto) {
        memberService.enrollMember(memberEnrollRequestDto);
        return ResponseEntity.ok(MemberResponseMessage.CREATED_USER.getMessage());
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginMember(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        memberService.login(loginRequestDto);
        return ResponseEntity.ok(MemberResponseMessage.LOGIN_SUCCESS.getMessage());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Refresh-Token") String refreshToken) {
        memberService.logout(refreshToken);
        return ResponseEntity.ok(MemberResponseMessage.LOGOUT_SUCCESS.getMessage());
    }
}

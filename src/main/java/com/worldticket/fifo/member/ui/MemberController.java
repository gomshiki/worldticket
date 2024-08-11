package com.worldticket.fifo.member.ui;

import com.worldticket.fifo.member.application.MemberService;
import com.worldticket.fifo.member.application.TokenAuthService;
import com.worldticket.fifo.member.dto.LoginRequestDto;
import com.worldticket.fifo.member.dto.MemberEnrollRequestDto;
import com.worldticket.fifo.member.dto.TokenResponseDto;
import com.worldticket.fifo.member.infra.MemberResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {
    private final MemberService memberService;
    private final TokenAuthService tokenAuthService;

    @PostMapping("/enroll")
    public ResponseEntity<Map<String, String>> enrollMember(@Valid @RequestBody MemberEnrollRequestDto memberEnrollRequestDto) {
        memberService.enrollMember(memberEnrollRequestDto);
        return ResponseEntity.ok(Map.of("message", MemberResponseMessage.CREATED_USER.getMessage()));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        TokenResponseDto tokenResponseDto = memberService.login(loginRequestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + tokenResponseDto.getAccessToken());
        httpHeaders.add("Refresh-Token", "Bearer " + tokenResponseDto.getRefreshToken());

        return ResponseEntity.ok().headers(httpHeaders).body(
                Map.of(
                        "access_token", tokenResponseDto.getAccessToken(),
                        "refresh_token", tokenResponseDto.getRefreshToken()
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String,String>> logout(@RequestHeader("Refresh-Token") String refreshToken) {
        memberService.logout(refreshToken);
        return ResponseEntity.ok(Map.of("message", MemberResponseMessage.LOGOUT_SUCCESS.getMessage()));

    }
}

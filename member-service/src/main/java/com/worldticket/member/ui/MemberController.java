package com.worldticket.member.ui;

import com.worldticket.member.application.AuthService;
import com.worldticket.member.application.EmailAuthService;
import com.worldticket.member.application.MemberService;
import com.worldticket.member.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/member-service")
@RestController
public class MemberController {
    private final Environment environment;
    private final MemberService memberService;
    private final EmailAuthService emailSendService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<MemberResponseDto> createMember(@Valid @RequestBody MemberEnrollRequestDto memberEnrollRequestDto) {
        MemberResponseDto memberResponseDto = memberService.createMember(memberEnrollRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberResponseDto);
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> getMembers() {
        List<MemberResponseDto> memberByAll = memberService.getMemberByAll();
        return ResponseEntity.ok(memberByAll);
    }

    @GetMapping("/members/{memberNumber}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable("memberNumber") UUID memberNumber){
        MemberResponseDto memberByMemberNumber = memberService.getMemberByMemberNumber(memberNumber);
        return ResponseEntity.ok(memberByMemberNumber);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginMember(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        memberService.login(loginRequestDto);
        return ResponseEntity.ok("로그인 성공");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Refresh-Token") String refreshToken) {
        try {
            memberService.logout(refreshToken);
            return ResponseEntity.ok("로그아웃 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed: " + e.getMessage());
        }
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's working in member-service on port %s",
                environment.getProperty("local.server.port"));
    }

    @GetMapping("/welcome")
    public String welcome(){
        return environment.getProperty("greeting.message");
    }

    @PostMapping("/email/send")
    public ResponseEntity<?> sendAuthCode(@RequestBody @Valid EmailRequestDto emailRequestDto) {
        emailSendService.sendAuthEmail(emailRequestDto.getEmail());
        return ResponseEntity.ok("이메일이 성공적으로 전송되었습니다.");
    }

    @PostMapping("/email/confirm")
    public ResponseEntity<?> confirmAuthCode(@RequestBody AuthCodeRequestDto authCodeRequestDto) {
        emailSendService.confirmCode(authCodeRequestDto);
        return ResponseEntity.ok("인증이 완료 되었습니다.");
    }

    @PostMapping("/token/issue")
    public ResponseEntity<Map<String, String>> issueToken(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        TokenResponseDto tokenResponseDto = authService.issueToken(loginRequestDto);
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

    @PostMapping("/token/reissue")
    public ResponseEntity<?> reissueToken(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
        TokenResponseDto ReissuedTokenDto = authService.reissueToken(tokenRequestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + ReissuedTokenDto.getAccessToken());
        httpHeaders.add("Refresh-Token", "Bearer " + ReissuedTokenDto.getRefreshToken());

        return ResponseEntity.ok().headers(httpHeaders).body(
                Map.of(
                        "access_token", ReissuedTokenDto.getAccessToken(),
                        "refresh_token", ReissuedTokenDto.getRefreshToken()
                )
        );
    }
}

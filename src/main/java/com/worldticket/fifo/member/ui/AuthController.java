package com.worldticket.fifo.member.ui;

import com.worldticket.fifo.globalutilities.provider.TokenProvider;
import com.worldticket.fifo.member.application.AuthService;
import com.worldticket.fifo.member.dto.AuthCodeRequestDto;
import com.worldticket.fifo.member.dto.EmailRequestDto;
import com.worldticket.fifo.member.application.EmailAuthService;
import com.worldticket.fifo.member.dto.LoginRequestDto;
import com.worldticket.fifo.member.dto.TokenResponseDto;
import com.worldticket.fifo.member.infra.MemberResponseMessage;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final EmailAuthService emailSendService;
    private final AuthService authService;

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



}

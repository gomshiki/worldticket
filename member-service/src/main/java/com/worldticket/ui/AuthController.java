package com.worldticket.ui;

import com.worldticket.application.AuthService;
import com.worldticket.application.EmailAuthService;
import com.worldticket.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
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

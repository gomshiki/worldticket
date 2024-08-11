package com.worldticket.fifo.member.ui;

import com.worldticket.fifo.member.application.TokenAuthService;
import com.worldticket.fifo.member.dto.AuthCodeRequestDto;
import com.worldticket.fifo.member.dto.EmailRequestDto;
import com.worldticket.fifo.member.application.EmailAuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final EmailAuthService emailSendService;
    private final TokenAuthService tokenAuthService;

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



}

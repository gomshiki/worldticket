package com.worldticket.common.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EncryptUtil {
    private final PasswordEncoder passwordEncoder;

    // 단방향 암호화
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    // 암호 검증
    public boolean validatePassword(String password, String savedPassword) {
        return passwordEncoder.matches(password, savedPassword);
    }
}

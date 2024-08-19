package com.worldticket.application;

import com.worldticket.dto.AuthCodeRequestDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class EmailAuthService {
    private static final Random random = new Random();

    private final RedisProvider redisProvider;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    public void sendAuthEmail(String email) {
        try {
            int authNumber = generateRandomNumber(6);
            String content = generateEmailContent(authNumber);
            sendEmail(username, email, "[worldticket] 회원 가입을 위한 이메일입니다", content);

            // redis 에 인증코드 저장
            redisProvider.setDataExpire(email, Integer.toString(authNumber), 60 * 3L);
        } catch (RuntimeException e) {
            throw new RuntimeException("메일 전송 중 에러가 발생했습니다.", e);
        }
    }

    private static String generateEmailContent(int authNumber) {
        return String.format("""
                    이메일 주소를 인증해주세요.
                    <br><br>
                    인증 번호는 '%d' 입니다.
                    <br>
                """, authNumber);
    }

    private int generateRandomNumber(int digits) {
        // 첫 번째 자릿수는 1 ~ 9사이에서 선택
        int randomNumber = random.nextInt(9) + 1;

        // 나머지 자릿수는 0 ~ 9 사이에서 선택
        for (int i = 1; i < digits; i++) {
            randomNumber = randomNumber * 10 + random.nextInt(10);
        }
        return randomNumber;
    }

    private void sendEmail(String setFrom, String toMail, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom); // service name
            helper.setTo(toMail); // customer email
            helper.setSubject(title); // email title
            helper.setText(content, true); // content, html: true
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("인증번호 이메일 전송에 실패했습니다.");
        }
    }

    public void confirmCode(AuthCodeRequestDto authCodeRequestDto) {
        String retrievedCode = redisProvider.getData(authCodeRequestDto.getEmail());
        if (!retrievedCode.equals(authCodeRequestDto.getCode())) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }
    }
}

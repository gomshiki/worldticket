package com.worldticket.fifo.member.infra;

import com.worldticket.fifo.member.dto.MemberEnrollRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@AllArgsConstructor
@Service
public class EncryptUtil {
    private final AesBytesEncryptor aesBytesEncryptor;
    private final PasswordEncoder passwordEncoder;

    // 이름 주소 이메일 전화번호 양방향 암호화, 비밀번호는 단방향
    public MemberEnrollRequestDto encryptMember(MemberEnrollRequestDto memberEnrollRequestDto) {
        String encryptPassword = encryptPassword(memberEnrollRequestDto.getPassword());
        String encryptName = encrypt(memberEnrollRequestDto.getMemberName());
        String encryptAddress1 = encrypt(memberEnrollRequestDto.getAddress1());
        String encryptAddress2 = encrypt(memberEnrollRequestDto.getAddress2());
        String encryptPhoneNumber = encrypt(memberEnrollRequestDto.getPhoneNumber());
        String encryptEmail = encrypt(memberEnrollRequestDto.getEmail());

        return new MemberEnrollRequestDto(
                encryptName, encryptEmail, encryptAddress1,
                encryptAddress2, encryptPhoneNumber, encryptPassword
        );
    }

    // 단방향 암호화
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    // 암호화
    private String encrypt(String target) {
        byte[] encrypt = aesBytesEncryptor.encrypt(target.getBytes(StandardCharsets.UTF_8));
        return byteArrayToString(encrypt);
    }

    // 복호화
    private String decrypt(String target) {
        byte[] decryptBytes = stringToByteArray(target);
        byte[] decrypt = aesBytesEncryptor.decrypt(decryptBytes);
        return new String(decrypt, StandardCharsets.UTF_8);
    }

    // byte -> String
    private String byteArrayToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte abyte : bytes) {
            sb.append(abyte);
            sb.append(" ");
        }
        return sb.toString();
    }

    // String -> byte
    private byte[] stringToByteArray(String byteString) {
        String[] split = byteString.split("\\s");
        ByteBuffer buffer = ByteBuffer.allocate(split.length);
        Arrays.stream(split).mapToInt(Integer::parseInt).forEach(
                s -> buffer.put((byte) s)
        );
        return buffer.array();
    }

}

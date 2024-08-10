package com.worldticket.fifo.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC) // 모듈로 나눌때 MODULE로 변경할것
@Entity
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone_number")
})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false)
    private String memberName;

    @Email(message = "유효한 이메일이 아닙니다.")
    @NotBlank
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String phoneNumber;

    @NotBlank
    @Column(nullable = false)
    private String address1;

    @NotBlank
    @Column(nullable = false)
    private String address2;

    @Column(updatable = false)
    private LocalDateTime createAt;

    @Column
    private LocalDateTime updateAt;

    @PrePersist
    protected void onCreate(){
        this.createAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updateAt = LocalDateTime.now();
    }

    public void encryptPassword(String password) {
        this.password = password;
    }
}

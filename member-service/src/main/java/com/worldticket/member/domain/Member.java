package com.worldticket.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC) // 모듈로 나눌때 MODULE로 변경할것
@Entity
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone_number"),
        @UniqueConstraint(columnNames = "memberNumber")
})
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String memberName;

    @Email(message = "유효한 이메일이 아닙니다.")
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address1;

    @Column(nullable = false)
    private String address2;

    @Column(nullable = false, updatable = false)
    private UUID memberNumber;

    @Column(updatable = false)
    private LocalDateTime createAt;

    @Column
    private LocalDateTime updateAt;

    @Column
    private String role;

    /*@OneToMany(mappedBy = "member")
    private List<Orders> ordersList;*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(String role : role.split(",")){
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    public void encryptPassword(String password) {
        this.password = password;
    }
}

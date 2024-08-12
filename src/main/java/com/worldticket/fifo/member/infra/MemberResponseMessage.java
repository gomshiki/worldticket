package com.worldticket.fifo.member.infra;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberResponseMessage {
    LOGIN_SUCCESS("로그인 성공"),
    LOGIN_FAIL("로그인 실패"),
    READ_USER("회원 정보 조회 성공"),
    NOT_FOUND_USER("회원을 찾을 수 없습니다."),
    DUPLICATE_USER("중복된 ID 입니다."),
    CREATED_USER("회원 가입 성공"),
    UPDATE_USER("회원 정보 수정 성공"),
    DELETE_USER("회원 탈퇴 성공"),
    INTERNAL_SERVER_ERROR("서버 내부 에러"),
    DB_ERROR("데이터베이스 에러"),
    LOGOUT_SUCCESS("로그아웃 성공");

    private final String message;
}

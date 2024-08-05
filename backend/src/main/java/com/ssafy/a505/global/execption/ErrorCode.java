package com.ssafy.a505.global.execption;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // TODO: 에러 추가 필요
    // Member
    INVALID_MEMBER_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 회원 아이디입니다."),
    ;
    private final HttpStatus httpStatus;
    private final String message;

}
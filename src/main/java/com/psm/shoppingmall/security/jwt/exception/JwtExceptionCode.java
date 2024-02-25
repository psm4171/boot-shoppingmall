package com.psm.shoppingmall.security.jwt.exception;

import lombok.Getter;

public enum JwtExceptionCode {

    NOT_FOUND_TOKEN("NOT_FOUND_TOKEN", "찾을 수 없는 토큰"),
    INVALID_TOKEN("INVALID_TOKEN", "유효하지 않은 토큰"),
    EXPIRED_TOKEN("EXPIRED_TOKEN", "기간이 만료된 토큰");

    @Getter
    private String code;
    @Getter
    private String message;

    JwtExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

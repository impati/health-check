package com.example.healthcheck.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 잘못됬습니다."),
    NOT_FOUND_SERVER(HttpStatus.NOT_FOUND, "찾을 수 없는 서버입니다."),
    HEALTH_CHECK_FAIL(HttpStatus.REQUEST_TIMEOUT,"헬스체크에 실패했습니다."),
    INVALID_ACCESS(HttpStatus.UNAUTHORIZED,"다른 사용자 영역에 침범했습니다."),
    SERVER_MISMATCH(HttpStatus.BAD_REQUEST,"입력한 서버 정보가 올바르지 않습니다.")
    ;
    private final HttpStatus status;
    private final String message;
}
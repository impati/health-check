package com.example.healthcheck.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "invalid Token"),
    NOT_FOUND_SERVER(HttpStatus.NOT_FOUND, "Not Found Server"),
    HEALTH_CHECK_FAIL(HttpStatus.REQUEST_TIMEOUT,"health check fail"),
    INVALID_ACCESS(HttpStatus.UNAUTHORIZED,"다른 사용자 영역에 침범했습니다."),
    SERVER_MISMATCH(HttpStatus.BAD_REQUEST,"입력한 서버 정보가 올바르지 않습니다."),
    UNSUPPORTED_ALARM_TYPE(HttpStatus.BAD_REQUEST,"지원하지 않는 알람입니다."),
    SEND_MAIL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"메일을 보내는데 실패했습니다."),
    FILL_MAIL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"메시지를 작성하는데 실패했습니다.")
    ;
    private final HttpStatus status;
    private final String message;
}
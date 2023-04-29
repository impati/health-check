package com.example.healthcheck.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "invalid Token"),
    NOT_FOUND_SERVER(HttpStatus.NOT_FOUND, "Not Found Server"),
    HEALTH_CHECK_FAIL(HttpStatus.REQUEST_TIMEOUT,"health check fail")
    ;
    private final HttpStatus status;
    private final String message;
}
package com.example.healthcheck.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckException extends RuntimeException {

	private ErrorCode errorCode;
	private String message;

	public HealthCheckException(final ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	@Override
	public String getMessage() {
		if (message == null) {
			return String.format("%s", errorCode.getMessage());
		}

		return String.format("%s %s", errorCode.getMessage(), message);
	}
}

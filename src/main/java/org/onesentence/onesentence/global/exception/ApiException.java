package org.onesentence.onesentence.global.exception;

import lombok.Getter;

@Getter
public abstract class ApiException extends RuntimeException {

	private final Integer statusCode;
	private final String message;

	protected ApiException(ExceptionStatus ex) {
		this.statusCode = ex.getStatusCode();
		this.message = ex.getMessage();
	}
}

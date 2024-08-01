package org.onesentence.onesentence.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionStatus {

	NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "존재하지 않습니다."),
	DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST.value(), "중복되는 닉네임입니다.");

	private final Integer statusCode;
	private final String message;
}

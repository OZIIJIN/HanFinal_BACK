package org.onesentence.onesentence.global.exception;

public class NotFoundException extends ApiException{

	public NotFoundException(ExceptionStatus ex) {
		super(ex);
	}

}

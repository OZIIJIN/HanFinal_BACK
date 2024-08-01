package org.onesentence.onesentence.global.exception;

public class DuplicatedException extends ApiException{

	public DuplicatedException(ExceptionStatus ex) {
		super(ex);
	}

}

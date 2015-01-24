package com.github.cecchisandrone.raspio.service;

public class IOServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public IOServiceException(String string) {
		super(string);
	}

	public IOServiceException(String string, Throwable t) {
		super(string, t);
	}
}

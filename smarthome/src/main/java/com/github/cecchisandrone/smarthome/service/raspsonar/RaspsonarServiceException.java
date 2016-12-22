package com.github.cecchisandrone.smarthome.service.raspsonar;

public class RaspsonarServiceException extends RuntimeException {

	private static final long serialVersionUID = -6792210802978874063L;

	public RaspsonarServiceException(String message) {
		super(message);
	}

	public RaspsonarServiceException(String message, Exception ex) {
		super(message, ex);
	}
}

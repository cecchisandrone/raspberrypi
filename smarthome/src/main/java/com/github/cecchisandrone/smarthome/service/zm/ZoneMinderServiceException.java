package com.github.cecchisandrone.smarthome.service.zm;

public class ZoneMinderServiceException extends RuntimeException {

	private static final long serialVersionUID = 1449006301471079359L;

	public ZoneMinderServiceException(String message, Exception ex) {
		super(message, ex);
	}

	public ZoneMinderServiceException(Exception ex) {
		super(ex);
	}
}

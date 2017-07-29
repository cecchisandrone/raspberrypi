package com.github.cecchisandrone.smarthome.service.gate;

public class GateServiceException extends Exception {

	private static final long serialVersionUID = 1449006301471079359L;

	public GateServiceException(String message, Exception ex) {
		super(message, ex);
	}

	public GateServiceException(Exception ex) {
		super(ex);
	}
}

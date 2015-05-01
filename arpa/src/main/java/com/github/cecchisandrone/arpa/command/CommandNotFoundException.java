package com.github.cecchisandrone.arpa.command;

public class CommandNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4493477776211106465L;

	public CommandNotFoundException(String error, Throwable t) {
		super("Command not found", t);
	}
}

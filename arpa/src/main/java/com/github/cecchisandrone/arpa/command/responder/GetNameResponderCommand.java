package com.github.cecchisandrone.arpa.command.responder;

import java.util.Map;

public class GetNameResponderCommand extends AbstractResponderCommand {

	private Map<String, String> contacts;

	public void setContacts(Map<String, String> contacts) {
		this.contacts = contacts;
	}

	@Override
	public String[] resolveArguments() {
		// TODO Auto-generated method stub
		return null;
	}

}

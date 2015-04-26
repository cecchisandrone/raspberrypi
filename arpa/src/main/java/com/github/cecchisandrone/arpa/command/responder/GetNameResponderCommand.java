package com.github.cecchisandrone.arpa.command.responder;

import java.util.List;
import java.util.Map;

import com.github.cecchisandrone.vc.wit.Contact;
import com.github.cecchisandrone.vc.wit.Outcome;

public class GetNameResponderCommand extends AbstractResponderCommand {

	private Map<String, String> contacts;

	public void setContacts(Map<String, String> contacts) {
		this.contacts = contacts;
	}

	@Override
	public String[] resolveArguments(Outcome outcome) {

		List<Contact> contactList = outcome.getEntities().getContacts();
		if (contactList != null && contacts.size() != 0) {
			Contact contact = contactList.get(0);
			String key = contact.getValue();
			return new String[] { contacts.get(key) };
		}
		return new String[] {};
	}
}

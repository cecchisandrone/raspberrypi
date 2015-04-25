package com.github.cecchisandrone.vc.wit;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Entities {

	@JsonProperty("contact")
	private List<Contact> contacts = new ArrayList<Contact>();

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContact(List<Contact> contacts) {
		this.contacts = contacts;
	}
}
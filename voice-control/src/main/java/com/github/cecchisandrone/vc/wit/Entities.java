package com.github.cecchisandrone.vc.wit;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Entities {

	@JsonProperty("contact")
	private List<Contact> contacts = new ArrayList<Contact>();

	@JsonProperty("language")
	private List<Language> languages = new ArrayList<Language>();

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContact(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public List<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}
}
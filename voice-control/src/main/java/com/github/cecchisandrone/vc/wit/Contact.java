package com.github.cecchisandrone.vc.wit;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contact {

	private String body;

	private Integer start;

	private String entity;

	private String value;

	private Integer end;

	private Boolean suggested;

	private String type;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public Boolean getSuggested() {
		return suggested;
	}

	public void setSuggested(Boolean suggested) {
		this.suggested = suggested;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
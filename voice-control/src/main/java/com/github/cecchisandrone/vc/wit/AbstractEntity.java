package com.github.cecchisandrone.vc.wit;

public class AbstractEntity {

	private String value;

	private Boolean suggested;

	private String type;

	private String metadata;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

}

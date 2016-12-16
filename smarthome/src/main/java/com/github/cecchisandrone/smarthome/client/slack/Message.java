
package com.github.cecchisandrone.smarthome.client.slack;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "username", "attachments", "mrkdwn", "text", "type", "subtype", "ts" })
public class Message {

	@JsonProperty("username")
	private String username;
	@JsonProperty("attachments")
	private List<Attachment> attachments = null;
	@JsonProperty("mrkdwn")
	private Boolean mrkdwn;
	@JsonProperty("text")
	private Object text;
	@JsonProperty("type")
	private String type;
	@JsonProperty("subtype")
	private String subtype;
	@JsonProperty("ts")
	private String ts;

	/**
	 * 
	 * @return The username
	 */
	@JsonProperty("username")
	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * @param username
	 *            The username
	 */
	@JsonProperty("username")
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 
	 * @return The attachments
	 */
	@JsonProperty("attachments")
	public List<Attachment> getAttachments() {
		return attachments;
	}

	/**
	 * 
	 * @param attachments
	 *            The attachments
	 */
	@JsonProperty("attachments")
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	/**
	 * 
	 * @return The mrkdwn
	 */
	@JsonProperty("mrkdwn")
	public Boolean getMrkdwn() {
		return mrkdwn;
	}

	/**
	 * 
	 * @param mrkdwn
	 *            The mrkdwn
	 */
	@JsonProperty("mrkdwn")
	public void setMrkdwn(Boolean mrkdwn) {
		this.mrkdwn = mrkdwn;
	}

	/**
	 * 
	 * @return The text
	 */
	@JsonProperty("text")
	public Object getText() {
		return text;
	}

	/**
	 * 
	 * @param text
	 *            The text
	 */
	@JsonProperty("text")
	public void setText(Object text) {
		this.text = text;
	}

	/**
	 * 
	 * @return The type
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 *            The type
	 */
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @return The subtype
	 */
	@JsonProperty("subtype")
	public String getSubtype() {
		return subtype;
	}

	/**
	 * 
	 * @param subtype
	 *            The subtype
	 */
	@JsonProperty("subtype")
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	/**
	 * 
	 * @return The ts
	 */
	@JsonProperty("ts")
	public String getTs() {
		return ts;
	}

	/**
	 * 
	 * @param ts
	 *            The ts
	 */
	@JsonProperty("ts")
	public void setTs(String ts) {
		this.ts = ts;
	}

}
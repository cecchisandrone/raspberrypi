package com.github.cecchisandrone.vc.wit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WitResponse {

	@JsonProperty("msg_id")
	private String msgId;

	@JsonProperty("_text")
	private String text;

	private Outcome[] outcomes;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Outcome[] getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(Outcome[] outcomes) {
		this.outcomes = outcomes;
	}
}

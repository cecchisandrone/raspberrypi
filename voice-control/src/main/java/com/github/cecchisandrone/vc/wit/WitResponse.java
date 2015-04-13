package com.github.cecchisandrone.vc.wit;

import com.fasterxml.jackson.annotation.JsonProperty;


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

package com.github.cecchisandrone.smarthome.client.slack;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelHistoryResponse extends SlackResponse {

	private List<Message> messages = null;

	@JsonProperty("has_more")
	private Boolean hasMore;

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Boolean getHasMore() {
		return hasMore;
	}

	public void setHasMore(Boolean hasMore) {
		this.hasMore = hasMore;
	}
}
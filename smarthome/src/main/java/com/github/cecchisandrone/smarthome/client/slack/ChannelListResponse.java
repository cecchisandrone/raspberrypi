package com.github.cecchisandrone.smarthome.client.slack;

import java.util.List;

public class ChannelListResponse extends SlackResponse {

	private List<Channel> channels = null;

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}
}
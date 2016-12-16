package com.github.cecchisandrone.smarthome.client.slack;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.cecchisandrone.smarthome.domain.SlackConfiguration;
import com.github.cecchisandrone.smarthome.utils.LoggingRequestInterceptor;

public class SlackClient {

	private static final String SLACK_API_URL = "https://slack.com/api/";

	private static final String CHANNEL_LIST_SUFFIX = "channels.list";

	private static final String CHANNEL_HISTORY_SUFFIX = "channels.history";

	private static final String CHAT_POST_MESSAGE_SUFFIX = "chat.postMessage";

	private RestTemplate restTemplate = new RestTemplate();

	public SlackClient() {
		ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor();
		List<ClientHttpRequestInterceptor> ris = new ArrayList<ClientHttpRequestInterceptor>();
		ris.add(ri);
		restTemplate.setInterceptors(ris);
		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
	}

	public ResponseEntity<ChannelListResponse> getChannelList(SlackConfiguration slackConfiguration) {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SLACK_API_URL + CHANNEL_LIST_SUFFIX)
				.queryParam("token", slackConfiguration.getToken()).queryParam("scope", "channels.list");

		return restTemplate.getForEntity(builder.build().encode().toUri(), ChannelListResponse.class);
	}

	public ResponseEntity<ChannelHistoryResponse> getLocationChangeChannelHistory(String channelId,
			SlackConfiguration slackConfiguration) {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SLACK_API_URL + CHANNEL_HISTORY_SUFFIX)
				.queryParam("token", slackConfiguration.getToken()).queryParam("scope", "channels.history")
				.queryParam("channel", channelId);

		return restTemplate.getForEntity(builder.build().encode().toUri(), ChannelHistoryResponse.class);
	}

	public ResponseEntity<SlackResponse> postMessageToAlarmChannel(SlackConfiguration slackConfiguration,
			String channelId, String message) {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SLACK_API_URL + CHAT_POST_MESSAGE_SUFFIX)
				.queryParam("token", slackConfiguration.getToken()).queryParam("channel", channelId)
				.queryParam("text", message).queryParam("as_user", "false").queryParam("username", "SmartHome")
				.queryParam("icon_url", "https://dl.dropboxusercontent.com/u/1580227/icons/home.png");

		return restTemplate.getForEntity(builder.build().encode().toUri(), SlackResponse.class);
	}
}

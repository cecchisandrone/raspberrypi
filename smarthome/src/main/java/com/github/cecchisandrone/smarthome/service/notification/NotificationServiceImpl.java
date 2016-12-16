package com.github.cecchisandrone.smarthome.service.notification;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.github.cecchisandrone.smarthome.client.slack.Channel;
import com.github.cecchisandrone.smarthome.client.slack.ChannelHistoryResponse;
import com.github.cecchisandrone.smarthome.client.slack.ChannelListResponse;
import com.github.cecchisandrone.smarthome.client.slack.Message;
import com.github.cecchisandrone.smarthome.client.slack.SlackClient;
import com.github.cecchisandrone.smarthome.client.slack.SlackResponse;
import com.github.cecchisandrone.smarthome.domain.LocationStatus;
import com.github.cecchisandrone.smarthome.domain.SlackConfiguration;
import com.github.cecchisandrone.smarthome.service.configuration.ConfigurationService;
import com.github.cecchisandrone.smarthome.service.configuration.ConfigurationServiceException;

import junit.framework.Assert;

@Service
public class NotificationServiceImpl implements NotificationService {

	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

	@Autowired
	private SlackClient slackClient;

	@Autowired
	private MailSender mailSender;

	@Autowired
	private ConfigurationService configurationService;

	private @Value("${application.name}") String applicationName;

	@Override
	public void sendMail(String to, String text) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(applicationName);
		message.setSubject(applicationName + " notification");
		message.setText(text);
		message.setTo(to);
		mailSender.send(message);
	}

	@Override
	public void sendSlackNotification(String message)
			throws ConfigurationServiceException, NotificationServiceException {
		try {
			sendSlackNotification(configurationService.getConfiguration().getSlackConfiguration(), message);
		} catch (ConfigurationServiceException e) {
			logger.error("Error while loading Slack configuration", e);
			throw e;
		}
	}

	@Override
	public void sendSlackNotification(SlackConfiguration slackConfiguration, String message)
			throws NotificationServiceException {

		Assert.assertNotNull(slackConfiguration);
		String channelId = getChannelId(slackConfiguration.getNotificationChannel(), slackConfiguration);
		ResponseEntity<SlackResponse> response = slackClient.postMessageToAlarmChannel(slackConfiguration, channelId,
				message);
		if (!response.getStatusCode().is2xxSuccessful() || !response.getBody().getOk()) {
			throw new NotificationServiceException("Unable to post message to Slack");
		}
	}

	@Override
	public Map<String, LocationStatus> getLocationStatus(SlackConfiguration slackConfiguration)
			throws NotificationServiceException {

		Assert.assertNotNull(slackConfiguration);

		Map<String, LocationStatus> locationStatus = new HashMap<String, LocationStatus>();

		if (slackConfiguration.getLocationChangeUsers() != null) {

			String[] users = slackConfiguration.getUsers();

			String channelId = getChannelId(slackConfiguration.getLocationChangeChannel(), slackConfiguration);
			ResponseEntity<ChannelHistoryResponse> response = slackClient.getLocationChangeChannelHistory(channelId,
					slackConfiguration);
			if (response.getStatusCode().is2xxSuccessful() && response.getBody().getOk()) {
				ChannelHistoryResponse history = response.getBody();

				for (Message message : history.getMessages()) {

					if (message.getAttachments() == null) {
						continue;
					}
					String text = message.getAttachments().get(0).getText();
					for (String user : users) {
						String[] tokens = text.split(" ");
						if (tokens[0].equals(user) && locationStatus.get(user) == null) {
							locationStatus.put(user, LocationStatus.valueOf(tokens[1].toUpperCase()));
							break;
						}
					}
					if (locationStatus.size() == users.length) {
						break;
					}
				}
				return locationStatus;

			} else {
				throw new NotificationServiceException("Unable to get location change history from Slack");
			}
		}
		return null;
	}

	private String getChannelId(String channelName, SlackConfiguration slackConfiguration) {
		ResponseEntity<ChannelListResponse> channelList = slackClient.getChannelList(slackConfiguration);
		for (Channel channel : channelList.getBody().getChannels()) {
			if (channel.getName().equals(channelName)) {
				return channel.getId();
			}
		}
		throw new IllegalArgumentException("Channel " + channelName + " not found");
	}
}

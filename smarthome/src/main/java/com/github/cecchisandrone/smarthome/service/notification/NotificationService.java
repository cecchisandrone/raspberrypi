package com.github.cecchisandrone.smarthome.service.notification;

import java.util.Map;

import com.github.cecchisandrone.smarthome.domain.LocationStatus;
import com.github.cecchisandrone.smarthome.domain.SlackConfiguration;
import com.github.cecchisandrone.smarthome.service.configuration.ConfigurationServiceException;

public interface NotificationService {

	void sendMail(String to, String text);

	void sendSlackNotification(String message) throws ConfigurationServiceException, NotificationServiceException;

	void sendSlackNotification(SlackConfiguration slackConfiguration, String message)
			throws NotificationServiceException;

	Map<String, LocationStatus> getLocationStatus(SlackConfiguration slackConfiguration)
			throws NotificationServiceException;

}
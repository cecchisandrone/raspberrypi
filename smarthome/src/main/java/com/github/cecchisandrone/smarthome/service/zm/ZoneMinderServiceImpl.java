package com.github.cecchisandrone.smarthome.service.zm;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.cecchisandrone.smarthome.domain.Configuration;
import com.github.cecchisandrone.smarthome.domain.LocationStatus;
import com.github.cecchisandrone.smarthome.domain.ZoneMinderConfiguration;
import com.github.cecchisandrone.smarthome.service.configuration.ConfigurationService;
import com.github.cecchisandrone.smarthome.service.configuration.ConfigurationServiceException;
import com.github.cecchisandrone.smarthome.service.notification.NotificationService;
import com.github.cecchisandrone.smarthome.service.notification.NotificationServiceException;
import com.github.cecchisandrone.smarthome.utils.MagicPacketSender;
import com.github.cecchisandrone.smarthome.utils.ZoneMinderUtils;

@Service
public class ZoneMinderServiceImpl implements ZoneMinderService {

	private static final Logger logger = LoggerFactory.getLogger(ZoneMinderServiceImpl.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private NotificationService notificationService;

	public void wakeUpZmHost(ZoneMinderConfiguration configuration) {
		try {
			Assert.notNull(configuration, "ZoneMinder configuration should be not null");
			MagicPacketSender.send(configuration.getZmHostMacAddress());
		} catch (IllegalArgumentException | IOException e) {
			throw new ZoneMinderServiceException("Error while waking up host " + configuration.getZmHostMacAddress()
					+ " - " + configuration.getZmHost(), e);
		}
	}

	public String shutdownZmHost(ZoneMinderConfiguration configuration) {
		Assert.notNull(configuration, "ZoneMinder configuration should be not null");
		try {
			if (ZoneMinderUtils.pingHost(configuration.getZmHost())) {
				return ZoneMinderUtils.shutdownZmHost(configuration.getZmHost(), configuration.getZmHostUser(),
						configuration.getZmHostPassword());
			}
		} catch (Exception e) {
			throw new ZoneMinderServiceException(
					"Unable to shutdown " + configuration.getZmHost() + " . Reason: " + e.toString(), e);
		}
		return null;
	}

	@Scheduled(fixedDelay = 60000, initialDelay = 60000)
	public void autoActivation() {
		try {
			logger.info("Checking if ZoneMinder automatic activation is needed");
			Configuration configuration = configurationService.getConfiguration();
			if (configuration.getZoneMinderConfiguration() != null
					&& configuration.getZoneMinderConfiguration().getAutomaticActivationEnabled()) {

				// Get latest location status for configured users
				Map<String, LocationStatus> status = notificationService
						.getLocationStatus(configuration.getSlackConfiguration());

				logger.info("Location status: " + status.toString());

				int exitedCount = 0;
				for (Entry<String, LocationStatus> entry : status.entrySet()) {
					if (entry.getValue().equals(LocationStatus.EXITED)) {
						exitedCount++;
					}

					// If at least one user entered the configured location,
					// shutdown ZoneMinder host
					if (entry.getValue().equals(LocationStatus.ENTERED)) {
						shutdownZmHost(configuration.getZoneMinderConfiguration());
						return;
					}
				}

				// If all users exited the configured location, wake up
				// ZoneMinder host
				if (exitedCount == configuration.getSlackConfiguration().getUsers().length) {
					wakeUpZmHost(configuration.getZoneMinderConfiguration());
				}

			} else {
				logger.info("ZoneMinder activation is not needed");
			}
		} catch (ConfigurationServiceException e) {
			logger.error("Unable to read configuration from Configuration Service", e);
		} catch (NotificationServiceException e) {
			logger.error("Error while reading location status", e);
		}
	}
}

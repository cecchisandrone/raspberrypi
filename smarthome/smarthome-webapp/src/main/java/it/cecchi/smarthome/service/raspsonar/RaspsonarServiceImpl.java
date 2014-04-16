package it.cecchi.smarthome.service.raspsonar;

import it.cecchi.smarthome.domain.Configuration;
import it.cecchi.smarthome.service.configuration.ConfigurationService;
import it.cecchi.smarthome.service.configuration.ConfigurationServiceException;
import it.cecchi.smarthome.service.notification.NotificationService;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RaspsonarServiceImpl implements InitializingBean, RaspsonarService {

	private static final int MEASUREMENTS = 5;

	private static final Logger logger = LoggerFactory.getLogger(RaspsonarServiceImpl.class);

	private double averageDistance;

	private WebTarget webTarget;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private NotificationService notificationService;

	@Override
	public void afterPropertiesSet() throws Exception {

		loadClientConfiguration();
	}

	private void loadClientConfiguration() throws ConfigurationServiceException {

		// Instantiate service client
		Client client = ClientBuilder.newClient();
		Configuration configuration = configurationService.getConfiguration();
		webTarget = client.target(configuration.getServiceUrl());
	}

	@Override
	public synchronized Double getDistance() throws RaspsonarServiceException {

		try {
			loadClientConfiguration();
			WebTarget getDistanceTarget = webTarget.queryParam("measurements", MEASUREMENTS);
			Builder request = getDistanceTarget.request();
			Response response = request.get();
			String distanceAsString = response.readEntity(String.class);
			Double distance = new Double(distanceAsString);
			if (averageDistance == 0) {
				averageDistance = distance;
			}
			averageDistance = (distance + averageDistance) / 2;
			return new BigDecimal(averageDistance).setScale(1, RoundingMode.CEILING).doubleValue();

		} catch (Exception e) {
			throw new RaspsonarServiceException("Can't access remote service. Reason: " + e.toString());
		}
	}

	// Every 2 hours
	@Override
	@Scheduled(cron = "0 0 0/2 * * ?")
	public void checkDistanceTask() {

		try {
			logger.info("Checking distance threshold...");
			Double distance = getDistance();
			logger.info("Distance is " + distance);
			Configuration configuration = configurationService.getConfiguration();
			if (distance > configuration.getDistanceThreshold()) {
				logger.info("Alerting user");
				notificationService.sendMail(configuration.getEmail(), "Warning! Distance threshold has been trespassed. Value: " + distance);
			}

		} catch (RaspsonarServiceException e) {
			logger.error(e.toString(), e);
		} catch (ConfigurationServiceException e) {
			logger.error(e.toString(), e);
		}
	}
}

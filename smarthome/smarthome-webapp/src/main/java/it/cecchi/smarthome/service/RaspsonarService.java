package it.cecchi.smarthome.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RaspsonarService {

	private static final int MEASUREMENTS = 5;

	private static final Logger logger = LoggerFactory.getLogger(RaspsonarService.class);

	private double averageDistance;

	public enum PropertyName {
		EMAIL("email"), SERVICE_URL("serviceUrl"), DISTANCE_THRESHOLD("distanceThreshold");

		private String propertyName;

		public String getPropertyName() {
			return propertyName;
		}

		private PropertyName(String propertyName) {
			this.propertyName = propertyName;
		}
	}

	private static final String PROPERTIES_FILENAME = "raspsonar.properties";

	private File configurationFile;

	private Properties properties;

	private WebTarget webTarget;

	@Autowired
	private NotificationService notificationService;

	public RaspsonarService() throws FileNotFoundException, IOException {

		// Configuration load
		properties = new Properties();
		File folder = new File(System.getProperty("user.home") + File.separator + ".raspsonar");
		configurationFile = new File(folder, PROPERTIES_FILENAME);
		if (folder.mkdir()) { // Create new
			configurationFile.createNewFile();
			properties = getDefaultProperties();
			properties.store(new FileOutputStream(configurationFile), "raspsonar configuration file");
		} else { // Load existing
			properties.load(new FileInputStream(configurationFile));
		}
		createServiceClient();
	}

	private Properties getDefaultProperties() {
		Properties def = new Properties();
		def.put(PropertyName.EMAIL.getPropertyName(), "testemail@test.mail");
		def.put(PropertyName.SERVICE_URL.getPropertyName(), "http://service_url:8080/service");
		def.put(PropertyName.DISTANCE_THRESHOLD.getPropertyName(), "10.0");
		return def;
	}

	private void createServiceClient() {
		// Instantiate service client
		Client client = ClientBuilder.newClient();
		webTarget = client.target(properties.getProperty(PropertyName.SERVICE_URL.getPropertyName()));
	}

	public File getConfigurationFile() {
		return configurationFile;
	}

	public synchronized Double getDistance() throws RaspsonarServiceException {

		try {
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

	public Properties getProperties() {
		return properties;
	}

	public void saveProperties(Properties properties) throws RaspsonarServiceException {
		try {
			properties.store(new FileOutputStream(configurationFile), "raspsonar configuration file");
			createServiceClient();
		} catch (FileNotFoundException e) {
			throw new RaspsonarServiceException("Can't find configuration file " + PROPERTIES_FILENAME);
		} catch (IOException e) {
			throw new RaspsonarServiceException("Unable to save properties. Reason: " + e.toString());
		}
	}

	// Every 2 hours
	@Scheduled(cron = "0 0 0/2 * * ?")
	public void checkDistanceTask() {

		try {
			logger.info("Checking distance threshold...");
			Double distance = getDistance();
			logger.info("Distance is " + distance);
			if (distance > new Double((String) properties.get(PropertyName.DISTANCE_THRESHOLD.getPropertyName()))) {
				logger.info("Alerting user");
				notificationService.sendMail((String) properties.get(PropertyName.EMAIL.getPropertyName()),
					"Warning! Distance threshold has been trespassed. Value: " + distance);
			}

		} catch (RaspsonarServiceException e) {
			logger.error(e.toString(), e);
		}
	}
}

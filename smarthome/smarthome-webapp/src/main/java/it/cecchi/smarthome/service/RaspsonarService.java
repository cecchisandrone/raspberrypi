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

import org.springframework.stereotype.Service;

@Service
public class RaspsonarService {

	public enum PropertyName {
		EMAIL("email"), SERVICE_URL("serviceUrl");

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

	public Double getWaterLevel() throws RaspsonarServiceException {

		try {
			Builder request = webTarget.request();
			Response response = request.get();
			String distanceAsString = response.readEntity(String.class);
			return new BigDecimal(distanceAsString).setScale(1, RoundingMode.CEILING).doubleValue();
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
}

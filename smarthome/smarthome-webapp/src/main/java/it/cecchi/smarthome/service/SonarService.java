package it.cecchi.smarthome.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Service;

@Service
public class SonarService {

	public enum PropertyName {
		EMAIL("email");

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

	public SonarService() throws FileNotFoundException, IOException {

		// Configuration load
		File folder = new File(System.getProperty("user.home") + File.separator + ".raspsonar");
		configurationFile = new File(folder, PROPERTIES_FILENAME);
		if (folder.mkdir()) {
			configurationFile.createNewFile();
		}
		properties = new Properties();
		properties.load(new FileInputStream(configurationFile));
	}

	public File getConfigurationFile() {
		return configurationFile;
	}

	public String getWaterLevel() {
		return "121 cm";
	}

	public Properties getProperties() {
		return properties;
	}

	public void saveProperties(Properties properties) throws SonarServiceException {
		try {
			properties.store(new FileOutputStream(configurationFile), "raspsonar configuration file");
		} catch (FileNotFoundException e) {
			throw new SonarServiceException("Can't find configuration file " + PROPERTIES_FILENAME);
		} catch (IOException e) {
			throw new SonarServiceException("Unable to save properties. Reason: " + e.toString());
		}
	}
}

package it.cecchi.raspsonar.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Service;

@Service
public class SonarService {

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
}

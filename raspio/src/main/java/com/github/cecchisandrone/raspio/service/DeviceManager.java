package com.github.cecchisandrone.raspio.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.cecchisandrone.raspio.gpio.AbstractDevice;

/**
 * Manages multiple {@link AbstractDevice} instances, using the configuration read from devices.properties file.
 * Devices are hold in the same order as the properties file and can be accessed by index 
 * 
 * @author Alessandro
 *
 */
public class DeviceManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceManager.class);

	private Map<String, AbstractDevice> devicesMap = new HashMap<String, AbstractDevice>();

	private DeviceFactory deviceFactory = new DeviceFactory();

	/**
	 * Initializes a map of devices reading devices.properties file with the following format:
	 * 
	 * ## Begin of property file ##
	 * sonar.1=1,2
	 * sonar.2=3,4
	 * motor.3=6,7,8,9,10,11
	 * relay.4=12
	 * ## End of property file ##
	 * 
	 * The value after the . represents the ID of the device in the map 
	 */
	public DeviceManager() {

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("devices.properties");
		String file = System.getProperty("devices.properties.file");
		Properties p = new Properties();

		try {
			if (file != null) { // Override classpath file with custom file
				File f = new File(file);
				inputStream = new FileInputStream(f);
			}

			if (inputStream == null) {
				throw new IllegalArgumentException(
						"devices.properties file not found in classpath and devices.properties.file system property not specified. Unable to initialize the device manager");
			}

			p.load(inputStream);
		} catch (IOException e) {
			LOGGER.error("Unable to load devices.properties", e);
		}

		for (Entry<Object, Object> entry : p.entrySet()) {
			try {

				String deviceKey = ((String) entry.getKey());
				if (!deviceKey.matches("\\w+\\.\\d+")) {
					throw new IllegalArgumentException("Invalid device string: " + entry.getKey());
				}

				String[] split = deviceKey.split("\\.");
				String deviceType = split[0];
				String id = split[1];

				AbstractDevice device = deviceFactory.createDevice(deviceType, (String) entry.getValue());
				devicesMap.put(id, device);
			} catch (DeviceFactoryException e) {
				LOGGER.error("Unable to get a device", e);
			}
		}
	}

	public AbstractDevice getDevice(Class clazz, String id) throws IOServiceException {

		AbstractDevice device = devicesMap.get(id);
		if (device == null) {
			throw new IOServiceException("Device with ID " + id + " not found. Available device IDs are: "
					+ devicesMap.keySet());
		}
		try {
			return (AbstractDevice) clazz.cast(device);
		} catch (ClassCastException e) {
			LOGGER.error("The device with " + id + " is not an instance of " + clazz);
			throw new IOServiceException("The device at address " + id + " is not an instance of " + clazz, e);
		}
	}
}

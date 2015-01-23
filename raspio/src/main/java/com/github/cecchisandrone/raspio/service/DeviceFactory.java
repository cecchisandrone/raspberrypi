package com.github.cecchisandrone.raspio.service;

import java.util.HashMap;
import java.util.Map;

import com.github.cecchisandrone.raspio.io.AbstractDevice;
import com.github.cecchisandrone.raspio.io.MotorDevice;
import com.github.cecchisandrone.raspio.io.RelayDevice;
import com.github.cecchisandrone.raspio.io.SonarDevice;

public class DeviceFactory {

	private Map<String, Class> deviceClasses;

	public DeviceFactory() {

		deviceClasses = new HashMap<String, Class>();
		deviceClasses.put("sonar", SonarDevice.class);
		deviceClasses.put("relay", RelayDevice.class);
		deviceClasses.put("motor", MotorDevice.class);
	}

	public AbstractDevice createDevice(String deviceString, String confString) throws DeviceFactoryException {

		if (!deviceString.contains(".")) {
			throw new IllegalArgumentException("Invalid device string: " + confString);
		}

		deviceString = deviceString.split("\\.")[0];

		// Instantiate device
		Class clazz = deviceClasses.get(deviceString);
		try {
			AbstractDevice device = (AbstractDevice) clazz.newInstance();
			device.initWithConfig(confString);
			return device;
		} catch (Exception e) {
			throw new DeviceFactoryException("Unable to create a device of type " + deviceString, e);
		}
	}
}

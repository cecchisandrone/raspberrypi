package com.github.cecchisandrone.raspio.service;

import java.util.HashMap;
import java.util.Map;

import com.github.cecchisandrone.raspio.gpio.AbstractDevice;
import com.github.cecchisandrone.raspio.gpio.MotorDevice;
import com.github.cecchisandrone.raspio.gpio.RelayDevice;
import com.github.cecchisandrone.raspio.gpio.ServoMotorDevice;
import com.github.cecchisandrone.raspio.gpio.SonarDevice;

public class DeviceFactory {

	private Map<String, Class> deviceClasses;

	public DeviceFactory() {

		deviceClasses = new HashMap<String, Class>();
		deviceClasses.put("sonar", SonarDevice.class);
		deviceClasses.put("relay", RelayDevice.class);
		deviceClasses.put("motor", MotorDevice.class);
		deviceClasses.put("servoMotor", ServoMotorDevice.class);
	}

	public AbstractDevice createDevice(String deviceType, String confString) throws DeviceFactoryException {

		// Instantiate device
		Class clazz = deviceClasses.get(deviceType);
		try {
			AbstractDevice device = (AbstractDevice) clazz.newInstance();
			device.initWithConfig(confString);
			return device;
		} catch (Exception e) {
			throw new DeviceFactoryException("Unable to create a device of type " + deviceType, e);
		}
	}
}

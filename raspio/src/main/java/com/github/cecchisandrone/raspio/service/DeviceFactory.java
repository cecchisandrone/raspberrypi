package com.github.cecchisandrone.raspio.service;

import java.util.HashMap;
import java.util.Map;

import com.github.cecchisandrone.raspio.io.AbstractDevice;
import com.github.cecchisandrone.raspio.io.RelayDevice;
import com.github.cecchisandrone.raspio.io.SonarDevice;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class DeviceFactory {

	private Map<String, Class> deviceClasses;

	public DeviceFactory() {

		deviceClasses = new HashMap<String, Class>();
		deviceClasses.put("sonar", SonarDevice.class);
		deviceClasses.put("relay", RelayDevice.class);
	}

	public AbstractDevice createDevice(String deviceString, String confString) throws DeviceFactoryException {

		if (!deviceString.contains(".")) {
			throw new IllegalArgumentException("Invalid device string: " + confString);
		}

		deviceString = deviceString.split("\\.")[0];

		// Prepare pins
		String[] pinNumbers = confString.split(",");
		Pin[] pins = new Pin[pinNumbers.length];
		for (int i = 0; i < pins.length; i++) {
			pins[i] = getPinByPinNumber(pinNumbers[i]);
		}

		// Instantiate device
		Class clazz = deviceClasses.get(deviceString);
		try {
			return (AbstractDevice) clazz.getConstructors()[0].newInstance(pins);
		} catch (Exception e) {
			throw new DeviceFactoryException("Unable to create a device of type " + deviceString, e);
		}
	}

	private Pin getPinByPinNumber(String pinNumber) {

		switch (new Integer(pinNumber)) {
		case 0:
			return RaspiPin.GPIO_00;
		case 1:
			return RaspiPin.GPIO_01;
		case 2:
			return RaspiPin.GPIO_02;
		case 3:
			return RaspiPin.GPIO_03;
		case 4:
			return RaspiPin.GPIO_04;
		case 5:
			return RaspiPin.GPIO_05;
		case 6:
			return RaspiPin.GPIO_06;
		case 7:
			return RaspiPin.GPIO_07;
		case 8:
			return RaspiPin.GPIO_08;
		case 9:
			return RaspiPin.GPIO_09;
		case 10:
			return RaspiPin.GPIO_10;
		case 11:
			return RaspiPin.GPIO_11;
		case 12:
			return RaspiPin.GPIO_12;
		case 13:
			return RaspiPin.GPIO_13;
		case 14:
			return RaspiPin.GPIO_14;
		case 15:
			return RaspiPin.GPIO_15;
		case 16:
			return RaspiPin.GPIO_16;
		case 17:
			return RaspiPin.GPIO_17;
		case 18:
			return RaspiPin.GPIO_18;
		case 19:
			return RaspiPin.GPIO_19;
		case 20:
			return RaspiPin.GPIO_20;
		default:
			throw new IllegalArgumentException("Unknown pin: " + pinNumber);
		}
	}
}

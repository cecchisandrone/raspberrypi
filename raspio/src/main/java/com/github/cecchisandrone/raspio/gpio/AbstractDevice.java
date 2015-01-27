package com.github.cecchisandrone.raspio.gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Abstract class that represent a device attached to GPIO pins, using pi4j library
 * 
 * @author Alessandro
 *
 */
public abstract class AbstractDevice {

	protected GpioController gpio = GpioFactory.getInstance();

	protected boolean initialized = false;

	/**
	 * Initialize device configuration
	 */
	public void init() {
		internalInit();
		initialized = true;
	}

	/**
	 * Initialize device configuration using a configuration string
	 */
	public void initWithConfig(String configurationString) {
		loadConfiguration(configurationString);
		init();
	}

	protected void checkInitialized() {
		if (!initialized) {
			throw new IllegalArgumentException("Device is not initialized. Please run init() before using it");
		}
	}

	/**
	 * To be implemented to handle internal device initialization
	 */
	protected abstract void internalInit();

	/**
	 * To be implemented to load the configuration of device/pins from a configuration string 
	 * 
	 * @param configurationString
	 */
	protected abstract void loadConfiguration(String configurationString);

	protected Pin getPinByPinNumber(String pinNumber) {

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
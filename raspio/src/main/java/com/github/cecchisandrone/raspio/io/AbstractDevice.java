package com.github.cecchisandrone.raspio.io;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public abstract class AbstractDevice {

	protected GpioController gpio = GpioFactory.getInstance();

	protected boolean initialized = false;

	protected String configurationString;

	public void init() {
		internalInit();
		initialized = true;
	}

	public void initWithConfig(String configurationString) {
		this.configurationString = configurationString;
		internalInit(configurationString);
		initialized = true;
	}

	protected void checkInitialized() {
		if (!initialized) {
			throw new IllegalArgumentException("Device is not initialized. Please run init() before using it");
		}
	}

	public abstract void internalInit();

	public abstract void internalInit(String configurationString);
}
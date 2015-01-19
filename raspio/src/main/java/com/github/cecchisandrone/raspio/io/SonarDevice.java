package com.github.cecchisandrone.raspio.io;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;

public class SonarDevice extends AbstractDevice {

	private Pin trigger;

	private Pin echo;

	private GpioPinDigitalOutput triggerPin;

	private GpioPinDigitalInput echoPin;

	public void setEcho(Pin echo) {
		this.echo = echo;
	}

	public void setTrigger(Pin trigger) {
		this.trigger = trigger;
	}

	/**
	 * 
	 * Trigger the Range Finder and return the result in cm
	 * 
	 * @return
	 */
	public double getRange() {

		try {
			// Sensor settling
			triggerPin.low();
			Thread.sleep(500);

			// Fire the trigger pulse
			triggerPin.high();
			Thread.sleep(1);
			triggerPin.low();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Wait for the result signal
		long startTime = 0, stopTime = 0;
		do {
			startTime = System.nanoTime();
		} while (echoPin.getState() == PinState.LOW);

		do {
			stopTime = System.nanoTime();
		} while (echoPin.getState() == PinState.HIGH);

		// Calculate distance
		return ((stopTime - startTime) * 340.0) / 20000000.0;
	}

	@Override
	public void internalInit() {
		// Sonar Sensor pins
		triggerPin = gpio.provisionDigitalOutputPin(trigger, "Sonar Sensor Trigger", PinState.LOW);
		echoPin = gpio.provisionDigitalInputPin(echo, "Sonar Sensor Echo", PinPullResistance.PULL_DOWN);
	}

	@Override
	public void internalInit(String configurationString) {
		// TODO Auto-generated method stub

	}
}

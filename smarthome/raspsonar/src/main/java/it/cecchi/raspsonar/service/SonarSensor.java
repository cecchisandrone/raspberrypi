package it.cecchi.raspsonar.service;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class SonarSensor {

	double result = 0;

	private GpioPinDigitalOutput triggerPin;

	private GpioPinDigitalInput echoPin;
	
	private GpioPinDigitalOutput relayPin;

	private static SonarSensor sonarSensor = null;

	private SonarSensor(GpioPinDigitalOutput triggerPin, GpioPinDigitalInput echoPin, GpioPinDigitalOutput relayPin) {

		this.triggerPin = triggerPin;
		this.echoPin = echoPin;
		this.relayPin = relayPin;
	}

	public static SonarSensor getInstance() {

		// Instantiate the range finder
		if (sonarSensor == null) {

			// Setup GPIO Pins
			GpioController gpio = GpioFactory.getInstance();

			// Sonar Sensor pins
			GpioPinDigitalOutput triggerPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Sonar Sensor Trigger", PinState.LOW);
			GpioPinDigitalInput echoPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, "Sonar Sensor Echo", PinPullResistance.PULL_DOWN);

			// Relay toggle
			GpioPinDigitalOutput relayPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, "Relay Toggle Pin", PinState.LOW);
			
			sonarSensor = new SonarSensor(triggerPin, echoPin, relayPin);
		}

		return sonarSensor;
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
		}
		while (echoPin.getState() == PinState.LOW);

		do {
			stopTime = System.nanoTime();
		}
		while (echoPin.getState() == PinState.HIGH);

		// Calculate distance
		return ((stopTime - startTime) * 340.0) / 20000000.0;
	}

	public void toggleRelay(boolean status) {
		relayPin.setState(status);		
	}
}

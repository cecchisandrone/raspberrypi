package it.cecchi.raspsonar.gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class RangeFinder {
	
	double result = 0;
	
	private GpioPinDigitalOutput triggerPin;
	
	private GpioPinDigitalInput echoPin;

	private static RangeFinder rangeFinder = null;

	private RangeFinder(GpioPinDigitalOutput triggerPin, GpioPinDigitalInput echoPin) {
		
		this.triggerPin = triggerPin;
		this.echoPin = echoPin;
	}
	
	public static RangeFinder getInstance() {

		// Return existing instance
		if (rangeFinder != null) {
			
			// Instantiate the range finder
	
			// Setup GPIO Pins
			GpioController gpio = GpioFactory.getInstance();
	
			// range finder pins
			GpioPinDigitalOutput triggerPin = gpio
					.provisionDigitalOutputPin(RaspiPin.GPIO_00,
							"Range Finder Trigger", PinState.LOW);
	
			GpioPinDigitalInput echoPin = gpio.provisionDigitalInputPin(
					RaspiPin.GPIO_03, "Range Pulse Result",
					PinPullResistance.PULL_DOWN);
					
			rangeFinder = new RangeFinder(triggerPin,echoPin);
		}

		return rangeFinder;
	}

	/**
	 * 
	 * Trigger the Range Finder and return the result
	 * 
	 * @return
	 */
	public double getRange() {
		System.out.println("Range Finder Triggered");
		try {
			// fire the trigger pulse
			triggerPin.high();

			Thread.sleep(20);
		} catch (InterruptedException e) {

			e.printStackTrace();
			System.out.println("Exception triggering range finder");
		}
		triggerPin.low();

		// wait for the result

		double startTime = System.currentTimeMillis();
		double stopTime = 0;
		do {

			stopTime = System.currentTimeMillis();
			if ((System.currentTimeMillis() - startTime) >= 40) {
				break;
			}
		} while (echoPin.getState() != PinState.HIGH);

		// calculate the range. If the loop stopped after 38 ms set the result
		// to -1 to show it timed out.

		if ((stopTime - startTime) <= 38) {
			result = (stopTime - startTime) * 165.7;
		} else {
			System.out.println("Timed out");
			result = -1;
		}
		return result;
	}
}
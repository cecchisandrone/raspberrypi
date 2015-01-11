package it.cecchi.raspio.io;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class RelayDevice implements Device {

	private GpioPinDigitalOutput relayPin;

	public RelayDevice(Pin relay) {

		// Setup GPIO Pins
		GpioController gpio = GpioFactory.getInstance();

		relayPin = gpio.provisionDigitalOutputPin(relay, "Relay Toggle Pin", PinState.HIGH);
	}

	public void toggleRelay(boolean status) {
		relayPin.setState(status);
	}
}

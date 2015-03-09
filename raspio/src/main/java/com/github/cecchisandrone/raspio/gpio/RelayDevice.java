package com.github.cecchisandrone.raspio.gpio;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class RelayDevice extends AbstractDevice {

	private Pin relay;

	private GpioPinDigitalOutput relayPin;

	private PinState defaultState;

	public void setRelay(Pin relay) {
		this.relay = relay;
	}

	public void toggleRelay(boolean status) {
		relayPin.setState(status);
	}

	@Override
	public void internalInit() {
		relayPin = gpio.provisionDigitalOutputPin(relay, "Relay Toggle Pin", defaultState);
	}

	/**
	 * Expect a configuration string like this: pin, defaultState. Where:
	 * - pin: integer representing pin to control the relay
	 * - defaultState: LOW or HIGH, representing the starting state
	 */
	@Override
	public void loadConfiguration(String configurationString) {

		String[] pinNumbers = configurationString.split(",");
		if (pinNumbers.length != 2) {
			throw new IllegalArgumentException("Bad format of configuration string");
		}
		String pin = pinNumbers[0];
		defaultState = PinState.valueOf(pinNumbers[1]);

		this.relay = getPinByPinNumber(pin);
	}
}

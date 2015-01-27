package com.github.cecchisandrone.raspio.gpio;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class RelayDevice extends AbstractDevice {

	private Pin relay;

	private GpioPinDigitalOutput relayPin;

	public void setRelay(Pin relay) {
		this.relay = relay;
	}

	public void toggleRelay(boolean status) {
		relayPin.setState(status);
	}

	@Override
	public void internalInit() {
		relayPin = gpio.provisionDigitalOutputPin(relay, "Relay Toggle Pin", PinState.HIGH);
	}

	/**
	 * Expect a configuration string like this: pin. Where:
	 * - pin: integer representing pin to control the relay
	 */
	@Override
	public void loadConfiguration(String configurationString) {

		this.relay = getPinByPinNumber(configurationString);
	}
}

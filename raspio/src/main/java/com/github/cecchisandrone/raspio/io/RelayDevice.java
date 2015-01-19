package com.github.cecchisandrone.raspio.io;

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

	@Override
	public void internalInit(String configurationString) {
		// TODO Auto-generated method stub

	}
}

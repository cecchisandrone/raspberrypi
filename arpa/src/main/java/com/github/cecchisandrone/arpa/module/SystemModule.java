package com.github.cecchisandrone.arpa.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.cecchisandrone.raspio.gpio.RelayDevice;
import com.github.cecchisandrone.raspio.input.JoypadController;
import com.github.cecchisandrone.raspio.input.JoypadController.Analog;
import com.github.cecchisandrone.raspio.input.JoypadController.Button;
import com.github.cecchisandrone.raspio.input.JoypadEvent;
import com.github.cecchisandrone.raspio.input.JoypadEventListener;
import com.github.cecchisandrone.raspio.service.DeviceManager;
import com.github.cecchisandrone.raspio.service.IOServiceException;

public class SystemModule extends AbstractAgentModule implements JoypadEventListener {

	@Autowired
	private JoypadController joypadController;

	@Autowired
	private DeviceManager deviceManager;

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemModule.class);

	private RelayDevice ledDevice;

	private String ledDeviceId;

	private boolean ledStatus = false;

	public void setLedDeviceId(String ledDeviceId) {
		this.ledDeviceId = ledDeviceId;
	}

	@Override
	public void init() {
		super.init();

		// Devices initialization
		try {
			ledDevice = (RelayDevice) deviceManager.getDevice(RelayDevice.class, ledDeviceId);
		} catch (IOServiceException e) {
			LOGGER.error(e.toString(), e);
		}

		// Joypad initialization
		joypadController.addEventListener(this);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				joypadController.disconnect();
			}
		});
		joypadController.connect();
	}

	@Override
	public void executeWork() {
	}

	public List<Button> getButtonsToNotify() {
		return Arrays.asList(Button.RB);
	}

	public List<Analog> getAnalogsToNotify() {
		return new ArrayList<JoypadController.Analog>();
	}

	@Override
	public void joypadEventTriggered(JoypadEvent event) {
		if (event.getChangedButton() != null && event.getChangedButton().equals(Button.RB) && event.getNewValue() == 1) {

			ledStatus = !ledStatus;
			ledDevice.toggleRelay(ledStatus);
		}
	}
}

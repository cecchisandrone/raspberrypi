package com.github.cecchisandrone.arpa.module;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.cecchisandrone.raspio.gpio.MotorDevice;
import com.github.cecchisandrone.raspio.gpio.MotorDevice.Motor;
import com.github.cecchisandrone.raspio.gpio.SonarDevice;
import com.github.cecchisandrone.raspio.input.JoypadController;
import com.github.cecchisandrone.raspio.input.JoypadController.Analog;
import com.github.cecchisandrone.raspio.input.JoypadController.Button;
import com.github.cecchisandrone.raspio.input.JoypadEvent;
import com.github.cecchisandrone.raspio.input.JoypadEventListener;
import com.github.cecchisandrone.raspio.service.DeviceManager;
import com.github.cecchisandrone.raspio.service.IOServiceException;

public class NavigationModule extends AbstractAgentModule implements Runnable, JoypadEventListener {

	private String motorDeviceId;

	private String sonarDeviceId;

	private int currentSpeedOnX = 0;

	private int currentSpeedOnY = 0;

	@Autowired
	private JoypadController joypadController;

	@Autowired
	private DeviceManager deviceManager;

	private MotorDevice motorDevice;

	private SonarDevice sonarDevice;

	private static final Logger LOGGER = LoggerFactory.getLogger(NavigationModule.class);

	public void setMotorDeviceId(String motorDeviceId) {
		this.motorDeviceId = motorDeviceId;
	}

	public void setSonarDeviceId(String sonarDeviceId) {
		this.sonarDeviceId = sonarDeviceId;
	}

	@Override
	public void init() {
		super.init();

		// Devices initialization
		try {
			motorDevice = (MotorDevice) deviceManager.getDevice(MotorDevice.class, motorDeviceId);
			sonarDevice = (SonarDevice) deviceManager.getDevice(SonarDevice.class, sonarDeviceId);
		} catch (IOServiceException e) {
			LOGGER.error(e.toString(), e);
		}

		// Joypad initialization
		joypadController.addEventListener(this);
		Runtime.getRuntime().addShutdownHook(new Thread(this));
		joypadController.connect();
	}

	@Override
	public void executeWork() {
		LOGGER.info("Executing NavigationModule");
	}

	@Override
	public void run() {
		joypadController.disconnect();
	}

	public List<Button> getButtonsToNotify() {
		return Arrays.asList(Button.values());
	}

	public List<Analog> getAnalogsToNotify() {
		return Arrays.asList(Analog.values());
	}

	@Override
	public void joypadEventTriggered(JoypadEvent event) {

		int speed = (int) ((100.0 / 32768.0) * event.getNewValue());

		if (Math.abs(speed) < 20) {
			speed = 0;
		}

		if (event.getChangedAnalog() != null && event.getChangedAnalog() == Analog.Y1 && currentSpeedOnX < 30) {
			currentSpeedOnY = Math.abs(speed);
			motorDevice.changeSpeed(Motor.LEFT, speed);
			motorDevice.changeSpeed(Motor.RIGHT, speed);
		} else if (event.getChangedAnalog() == Analog.X1 && currentSpeedOnY < 30) {
			currentSpeedOnX = Math.abs(speed);
			motorDevice.changeSpeed(Motor.LEFT, -speed);
			motorDevice.changeSpeed(Motor.RIGHT, speed);
		}
	}
}

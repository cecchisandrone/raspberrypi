package com.github.cecchisandrone.arpa.module;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.cecchisandrone.raspio.gpio.ServoMotorDevice;
import com.github.cecchisandrone.raspio.input.JoypadController;
import com.github.cecchisandrone.raspio.input.JoypadController.Analog;
import com.github.cecchisandrone.raspio.input.JoypadController.Button;
import com.github.cecchisandrone.raspio.input.JoypadEvent;
import com.github.cecchisandrone.raspio.input.JoypadEventListener;
import com.github.cecchisandrone.raspio.service.DeviceManager;
import com.github.cecchisandrone.raspio.service.IOServiceException;

public class CameraModule extends AbstractAgentModule implements JoypadEventListener {

	@Autowired
	private JoypadController joypadController;

	private static final Logger LOGGER = LoggerFactory.getLogger(CameraModule.class);

	private String servoMotorPanDeviceId;

	private String servoMotorTiltDeviceId;

	private ServoMotorDevice panMotor;

	private ServoMotorDevice tiltMotor;

	@Autowired
	private DeviceManager deviceManager;

	public void setServoMotorPanDeviceId(String servoMotorPanDeviceId) {
		this.servoMotorPanDeviceId = servoMotorPanDeviceId;
	}

	public void setServoMotorTiltDeviceId(String servoMotorTiltDeviceId) {
		this.servoMotorTiltDeviceId = servoMotorTiltDeviceId;
	}

	@Override
	public void init() {
		super.init();

		// Devices initialization
		try {
			panMotor = (ServoMotorDevice) deviceManager.getDevice(ServoMotorDevice.class, servoMotorPanDeviceId);
			tiltMotor = (ServoMotorDevice) deviceManager.getDevice(ServoMotorDevice.class, servoMotorTiltDeviceId);
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
		return Arrays.asList(Button.GUIDE);
	}

	public List<Analog> getAnalogsToNotify() {
		return Arrays.asList(Analog.X2, Analog.Y2);
	}

	@Override
	public void joypadEventTriggered(JoypadEvent event) {

		if (event.getChangedButton() != null && event.getChangedButton().equals(Button.GUIDE)) {
			try {
				Runtime.getRuntime().exec("./home/pi/scripts/face_detector_launcher.sh");
			} catch (IOException e) {
				LOGGER.error("Unable to start face detection. Reason: " + e.toString(), e);
			}
		}

		if (event.getChangedAnalog() != null) {

			// Normalize to 100
			int position = (int) ((100.0 / 65536.0) * (event.getNewValue() + 32768.0));
			System.out.println(position);

			if (event.getChangedAnalog().equals(Analog.X2)) {
				panMotor.changePosition(position);
			} else if (event.getChangedAnalog().equals(Analog.Y2)) {
				tiltMotor.changePosition(position);
			}
		}
	}
}

package com.github.cecchisandrone.arpa.module;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cecchisandrone.arpa.module.json.FrameConfig;
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

	private static final int POSITION_INCREASE = 1;

	private static final String CONFIG_URL_SUFFIX = "/config/frame";

	private static final String FACES_URL_SUFFIX = "/faces";

	private String servoMotorPanDeviceId;

	private String servoMotorTiltDeviceId;

	private ServoMotorDevice panMotor;

	private ServoMotorDevice tiltMotor;

	private int currentPanPosition = 50;

	private int currentTiltPosition = 100;

	private int currentPanIncrease = 0;

	private int currentTiltIncrease = 0;

	private ObjectMapper objectMapper = new ObjectMapper();

	private String host;

	private int port;

	private FrameConfig frameConfig;

	private Process faceDetectorProcess;

	@Autowired
	private DeviceManager deviceManager;

	public void setServoMotorPanDeviceId(String servoMotorPanDeviceId) {
		this.servoMotorPanDeviceId = servoMotorPanDeviceId;
	}

	public void setServoMotorTiltDeviceId(String servoMotorTiltDeviceId) {
		this.servoMotorTiltDeviceId = servoMotorTiltDeviceId;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void init() {
		super.init();

		// Devices initialization
		try {
			panMotor = (ServoMotorDevice) deviceManager.getDevice(ServoMotorDevice.class, servoMotorPanDeviceId);
			tiltMotor = (ServoMotorDevice) deviceManager.getDevice(ServoMotorDevice.class, servoMotorTiltDeviceId);

			// Initial position
			tiltMotor.changePosition(currentTiltPosition);
			panMotor.changePosition(currentPanPosition);
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

		// Camera move
		if (currentPanIncrease != 0 && currentPanPosition + currentPanIncrease > 0
				&& currentPanPosition + currentPanIncrease < ServoMotorDevice.POSITION_RANGE) {
			panMotor.changePosition(currentPanPosition += currentPanIncrease);
		} else if (currentTiltIncrease != 0 && currentTiltPosition + currentTiltIncrease > 0
				&& currentTiltPosition + currentTiltIncrease < ServoMotorDevice.POSITION_RANGE) {
			tiltMotor.changePosition(currentTiltPosition += currentTiltIncrease);
		}
	}

	private String getServerUrl() {
		return "http://" + host + ":" + port;
	}

	public List<Button> getButtonsToNotify() {
		return Arrays.asList(Button.GUIDE);
	}

	public List<Analog> getAnalogsToNotify() {
		return Arrays.asList(Analog.X3, Analog.Y3);
	}

	@Override
	public void joypadEventTriggered(JoypadEvent event) {

		if (event.getChangedButton() != null && event.getChangedButton().equals(Button.GUIDE)
				&& event.getNewValue() == 1) {

			if (faceDetectorProcess == null) {

				try {
					faceDetectorProcess = Runtime.getRuntime().exec(
							"sudo python /home/pi/raspberrypi/face-detector/face_detector.py -H " + host);
					// Wait to be started
					Thread.sleep(10000);
					frameConfig = objectMapper
							.readValue(new URL(getServerUrl() + CONFIG_URL_SUFFIX), FrameConfig.class);
				} catch (IOException | InterruptedException ex) {
					LOGGER.error("Unable to start face detection. Reason: " + ex.toString(), ex);
				}
			} else {
				frameConfig = null;
				faceDetectorProcess.destroy();
				faceDetectorProcess = null;
			}
		}

		if (event.getChangedAnalog() != null) {

			int value = 0;
			if (event.getNewValue() > 0) {
				value = POSITION_INCREASE;
			} else if (event.getNewValue() < 0) {
				value = -POSITION_INCREASE;
			}
			if (event.getChangedAnalog().equals(Analog.X3)) {
				currentPanIncrease = value;
			} else if (event.getChangedAnalog().equals(Analog.Y3)) {
				currentTiltIncrease = value;
			}
		}
	}
}

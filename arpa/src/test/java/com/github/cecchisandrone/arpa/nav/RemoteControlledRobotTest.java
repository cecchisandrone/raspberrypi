package com.github.cecchisandrone.arpa.nav;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.cecchisandrone.arpa.input.JoypadController;
import com.github.cecchisandrone.arpa.input.JoypadController.Analog;
import com.github.cecchisandrone.arpa.input.JoypadController.Button;
import com.github.cecchisandrone.arpa.input.JoypadEvent;
import com.github.cecchisandrone.arpa.input.JoypadEventListener;
import com.github.cecchisandrone.raspio.io.MotorDevice;
import com.github.cecchisandrone.raspio.io.MotorDevice.Motor;
import com.pi4j.io.gpio.RaspiPin;

public class RemoteControlledRobotTest implements Runnable, JoypadEventListener {

	private double currentSpeedOnX = 0;

	private double currentSpeedOnY = 0;

	private JoypadController controller;

	private MotorDevice motorDevice = new MotorDevice();

	public static void main(String[] args) throws IOException {

		RemoteControlledRobotTest launcher = new RemoteControlledRobotTest();
		Runtime.getRuntime().addShutdownHook(new Thread(launcher));
		launcher.launch();
	}

	private void launch() throws IOException {

		motorDevice.setEn1Pin(15);
		motorDevice.setEn2Pin(16);
		motorDevice.setIn1Pin(RaspiPin.GPIO_00);
		motorDevice.setIn2Pin(RaspiPin.GPIO_02);
		motorDevice.setIn3Pin(RaspiPin.GPIO_12);
		motorDevice.setIn4Pin(RaspiPin.GPIO_03);
		motorDevice.init();

		controller = JoypadController.getInstance("/dev/input/js0");
		controller.addEventListener(this);
		controller.connect();
	}

	public void run() {
		try {
			controller.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void joypadEventTriggered(JoypadEvent e) {

		int speed = (int) ((100.0 / 32768.0) * e.getNewValue());

		if (Math.abs(speed) < 20) {
			speed = 0;
		}

		if (e.getChangedAnalog() == Analog.Y1 && currentSpeedOnX < 30) {
			currentSpeedOnY = Math.abs(speed);
			motorDevice.changeSpeed(Motor.LEFT, speed);
			motorDevice.changeSpeed(Motor.RIGHT, speed);
		} else if (e.getChangedAnalog() == Analog.X1 && currentSpeedOnY < 30) {
			currentSpeedOnX = Math.abs(speed);
			motorDevice.changeSpeed(Motor.LEFT, -speed);
			motorDevice.changeSpeed(Motor.RIGHT, speed);
		}
	}

	public List<Button> getButtonsToNotify() {
		return new ArrayList<JoypadController.Button>();
	}

	public List<Analog> getAnalogsToNotify() {
		return Arrays.asList(Analog.values());
	}
}

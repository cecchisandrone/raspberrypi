package com.github.cecchisandrone.arpa.nav;

import java.util.Arrays;
import java.util.List;

import com.github.cecchisandrone.raspio.gpio.MotorDevice;
import com.github.cecchisandrone.raspio.gpio.MotorDevice.Motor;
import com.github.cecchisandrone.raspio.gpio.SonarDevice;
import com.github.cecchisandrone.raspio.input.JoypadController;
import com.github.cecchisandrone.raspio.input.JoypadController.Analog;
import com.github.cecchisandrone.raspio.input.JoypadController.Button;
import com.github.cecchisandrone.raspio.input.JoypadEvent;
import com.github.cecchisandrone.raspio.input.JoypadEventListener;
import com.pi4j.io.gpio.RaspiPin;

public class RemoteControlledRobotTest implements Runnable, JoypadEventListener {

	private int currentSpeedOnX = 0;

	private int currentSpeedOnY = 0;

	private JoypadController controller;

	private MotorDevice motorDevice = new MotorDevice();

	private SonarDevice sonarDevice = new SonarDevice();

	private boolean manualMode = true;

	public static void main(String[] args) {

		RemoteControlledRobotTest launcher = new RemoteControlledRobotTest();
		Runtime.getRuntime().addShutdownHook(new Thread(launcher));
		launcher.launch();
	}

	private void launch() {

		motorDevice.setEn1Pin(15);
		motorDevice.setEn2Pin(16);
		motorDevice.setIn1Pin(RaspiPin.GPIO_00);
		motorDevice.setIn2Pin(RaspiPin.GPIO_02);
		motorDevice.setIn3Pin(RaspiPin.GPIO_12);
		motorDevice.setIn4Pin(RaspiPin.GPIO_03);
		motorDevice.init();

		sonarDevice.setTrigger(RaspiPin.GPIO_13);
		sonarDevice.setEcho(RaspiPin.GPIO_14);
		sonarDevice.init();

		controller = JoypadController.getInstance("/dev/input/js0");
		controller.addEventListener(this);
		controller.connect();

		while (true) {
			if (!manualMode) {

				double range = sonarDevice.getRange();
				System.out.println(range);
				if (range < 30) {
					motorDevice.changeSpeed(Motor.LEFT, 40);
					motorDevice.changeSpeed(Motor.RIGHT, 40);
				}
			}
		}
	}

	public void run() {
		controller.disconnect();
	}

	public void joypadEventTriggered(JoypadEvent e) {

		if (e.getChangedButton() != null && e.getChangedButton().equals(Button.START) && e.getNewValue() == 1) {
			manualMode = !manualMode;
		}

		int speed = (int) ((100.0 / 32768.0) * e.getNewValue());

		if (Math.abs(speed) < 20) {
			speed = 0;
		}

		if (e.getChangedAnalog() != null && e.getChangedAnalog() == Analog.Y1 && currentSpeedOnX < 30) {
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
		return Arrays.asList(Button.values());
	}

	public List<Analog> getAnalogsToNotify() {
		return Arrays.asList(Analog.values());
	}
}

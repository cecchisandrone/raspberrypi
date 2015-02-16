package com.github.cecchisandrone.arpa.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.cecchisandrone.arpa.io.RobotNavigator;
import com.github.cecchisandrone.arpa.util.Utils;
import com.github.cecchisandrone.raspio.input.JoypadController;
import com.github.cecchisandrone.raspio.input.JoypadController.Analog;
import com.github.cecchisandrone.raspio.input.JoypadController.Button;
import com.github.cecchisandrone.raspio.input.JoypadEvent;
import com.github.cecchisandrone.raspio.input.JoypadEventListener;

public class AutonomousNavigationModule extends AbstractAgentModule implements JoypadEventListener {

	private static final double DEFAULT_OBSTACLE_THRESHOLD = 50;

	private static final int DEFAULT_SPEED = 50;

	private enum NavigationStatus {
		STOPPED, RUNNING, ROTATING, IDLE
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(AutonomousNavigationModule.class);

	@Autowired
	private RobotNavigator robotNavigator;

	private NavigationStatus navigationStatus = NavigationStatus.IDLE;

	@Autowired
	private JoypadController joypadController;

	private int navigationSpeed = DEFAULT_SPEED;

	private double obstacleThreshold = DEFAULT_OBSTACLE_THRESHOLD;

	@Override
	public void init() {
		super.init();

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

		switch (navigationStatus) {
		case RUNNING: {
			double range = robotNavigator.getRange();
			if (range < obstacleThreshold) {
				navigationStatus = NavigationStatus.ROTATING;
			}
			robotNavigator.moveStraight(-navigationSpeed);
			break;
		}
		case STOPPED:
			robotNavigator.stop();
			navigationStatus = NavigationStatus.IDLE;
			break;
		case ROTATING:
			robotNavigator.rotate(Utils.getRandom(-Math.PI, Math.PI));
			navigationStatus = NavigationStatus.RUNNING;
			break;
		case IDLE:
			navigationSpeed = DEFAULT_SPEED;
			break;
		}
	}

	public List<Button> getButtonsToNotify() {
		return Arrays.asList(Button.START, Button.BACK, Button.A, Button.B, Button.X, Button.Y);
	}

	public List<Analog> getAnalogsToNotify() {
		return new ArrayList<JoypadController.Analog>();
	}

	@Override
	public void joypadEventTriggered(JoypadEvent event) {

		// Enable autonomous navigation
		if (event.getChangedButton().equals(Button.START) && event.getNewValue() == 1) {
			navigationStatus = NavigationStatus.RUNNING;
		}

		// Disable autonomous navigation
		if (event.getChangedButton().equals(Button.BACK) && event.getNewValue() == 1) {
			navigationStatus = NavigationStatus.STOPPED;
		}

		// Increase speed by 10
		if (event.getChangedButton().equals(Button.A) && event.getNewValue() == 1) {
			navigationSpeed += 10;
		}

		// Decrease speed by 10
		if (event.getChangedButton().equals(Button.B) && event.getNewValue() == 1) {
			navigationSpeed -= 10;
		}

		// Increase threshold by 10
		if (event.getChangedButton().equals(Button.X) && event.getNewValue() == 1) {
			obstacleThreshold += 10;
		}

		// Decrease threshold by 10
		if (event.getChangedButton().equals(Button.Y) && event.getNewValue() == 1) {
			obstacleThreshold -= 10;
		}
	}
}

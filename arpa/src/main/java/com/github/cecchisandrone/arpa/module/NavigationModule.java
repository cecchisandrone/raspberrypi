package com.github.cecchisandrone.arpa.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.cecchisandrone.arpa.io.RobotNavigator;
import com.github.cecchisandrone.raspio.input.JoypadController;
import com.github.cecchisandrone.raspio.input.JoypadController.Analog;
import com.github.cecchisandrone.raspio.input.JoypadController.Button;
import com.github.cecchisandrone.raspio.input.JoypadEvent;
import com.github.cecchisandrone.raspio.input.JoypadEventListener;

public class NavigationModule extends AbstractAgentModule implements JoypadEventListener {

	@Autowired
	private JoypadController joypadController;

	@Autowired
	private RobotNavigator robotNavigator;

	private static final Logger LOGGER = LoggerFactory.getLogger(NavigationModule.class);

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
	}

	public List<Button> getButtonsToNotify() {
		return new ArrayList<JoypadController.Button>();
	}

	public List<Analog> getAnalogsToNotify() {
		return Arrays.asList(Analog.X1, Analog.Y1);
	}

	@Override
	public void joypadEventTriggered(JoypadEvent event) {

		int speed = (int) ((100.0 / 32768.0) * event.getNewValue());

		if (Math.abs(speed) < 20) { // Don't drive motors with speed < 20
			speed = 0;
		}

		if (event.getChangedAnalog() != null && event.getChangedAnalog() == Analog.Y1
				&& robotNavigator.getCurrentSpeedOnX() < 30) {
			robotNavigator.moveStraight(speed);
		} else if (event.getChangedAnalog() == Analog.X1 && robotNavigator.getCurrentSpeedOnY() < 30) {
			robotNavigator.rotate(speed);
		}
	}
}

package it.cecchi.arpa.input;

import it.cecchi.arpa.input.JoypadController.Analog;
import it.cecchi.arpa.input.JoypadController.Button;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JoypadEventLogger implements JoypadEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(JoypadEventLogger.class);

	private int loggedEvents = 0;

	public void joypadEventTriggered(JoypadEvent e) {

		loggedEvents++;
		LOGGER.info("Key {} has changed its value from " + e.getOldValue() + " to " + e.getNewValue(),
				e.getChangedButton() != null ? e.getChangedButton() : e.getChangedAnalog());
	}

	public List<Button> getButtonsToNotify() {
		return Arrays.asList(Button.values());
	}

	public List<Analog> getAnalogsToNotify() {
		return Arrays.asList(Analog.values());
	}

	public int getLoggedEvents() {
		return loggedEvents;
	}
}

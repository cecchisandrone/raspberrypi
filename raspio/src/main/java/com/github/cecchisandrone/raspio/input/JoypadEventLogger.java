package com.github.cecchisandrone.raspio.input;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.cecchisandrone.raspio.input.JoypadController.Analog;
import com.github.cecchisandrone.raspio.input.JoypadController.Button;

public class JoypadEventLogger implements JoypadEventListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(JoypadEventLogger.class);

  private int loggedEvents = 0;

  @Override
  public void joypadEventTriggered(JoypadEvent e) {

    loggedEvents++;
    LOGGER.info("Key {} has changed its value from " + e.getOldValue() + " to " + e.getNewValue(),
        e.getChangedButton() != null ? e.getChangedButton() : e.getChangedAnalog());
  }

  @Override
  public List<Button> getButtonsToNotify() {
    return Arrays.asList(Button.values());
  }

  @Override
  public List<Analog> getAnalogsToNotify() {
    return Arrays.asList(Analog.values());
  }

  public int getLoggedEvents() {
    return loggedEvents;
  }
}

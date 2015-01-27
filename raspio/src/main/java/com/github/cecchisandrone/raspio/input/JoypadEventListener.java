package com.github.cecchisandrone.raspio.input;

import java.util.List;

import com.github.cecchisandrone.raspio.input.JoypadController.Analog;
import com.github.cecchisandrone.raspio.input.JoypadController.Button;

public interface JoypadEventListener {

	public void joypadEventTriggered(JoypadEvent e);

	public List<Button> getButtonsToNotify();

	public List<Analog> getAnalogsToNotify();
}

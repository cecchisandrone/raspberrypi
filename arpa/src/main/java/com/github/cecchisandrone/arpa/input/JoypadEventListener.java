package com.github.cecchisandrone.arpa.input;

import java.util.List;

import com.github.cecchisandrone.arpa.input.JoypadController.Analog;
import com.github.cecchisandrone.arpa.input.JoypadController.Button;

public interface JoypadEventListener {

	public void joypadEventTriggered(JoypadEvent e);

	public List<Button> getButtonsToNotify();

	public List<Analog> getAnalogsToNotify();
}

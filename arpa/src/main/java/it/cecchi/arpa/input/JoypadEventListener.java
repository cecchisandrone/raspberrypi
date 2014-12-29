package it.cecchi.arpa.input;

import it.cecchi.arpa.input.JoypadController.Analog;
import it.cecchi.arpa.input.JoypadController.Button;

import java.util.List;

public interface JoypadEventListener {

	public void joypadEventTriggered(JoypadEvent e);

	public List<Button> getButtonsToNotify();

	public List<Analog> getAnalogsToNotify();
}

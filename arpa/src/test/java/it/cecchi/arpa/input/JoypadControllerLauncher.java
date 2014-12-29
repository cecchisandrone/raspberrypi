package it.cecchi.arpa.input;

import java.io.IOException;

public class JoypadControllerLauncher {

	public static void main(String[] args) throws IOException {
		JoypadController c = JoypadController.getInstance("/dev/input/js0");
		c.addEventListener(new JoypadEventLogger());
		c.connect();
	}
}

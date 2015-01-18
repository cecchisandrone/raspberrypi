package com.github.cecchisandrone.arpa.input;

import java.io.IOException;

import com.github.cecchisandrone.arpa.input.JoypadController;
import com.github.cecchisandrone.arpa.input.JoypadEventLogger;

public class JoypadControllerLauncher implements Runnable {

	private JoypadController controller;

	public static void main(String[] args) throws IOException {

		JoypadControllerLauncher launcher = new JoypadControllerLauncher();
		Runtime.getRuntime().addShutdownHook(new Thread(launcher));
		launcher.launch();
	}

	private void launch() throws IOException {
		controller = JoypadController.getInstance("/dev/input/js0");
		controller.addEventListener(new JoypadEventLogger());
		controller.connect();
	}

	public void run() {
		try {
			controller.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

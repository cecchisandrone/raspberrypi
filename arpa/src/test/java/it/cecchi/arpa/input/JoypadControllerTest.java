package it.cecchi.arpa.input;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class JoypadControllerTest {

	private static JoypadController joypadController;

	private static JoypadEventLogger joypadEventLogger;

	@BeforeClass
	public static void beforeClass() throws IOException {

		joypadController = JoypadController
				.getInstance(JoypadControllerTest.class.getResource("/joypad.out").getFile());

		joypadEventLogger = new JoypadEventLogger();
	}

	@Test
	public void testConnect() throws IOException {

		joypadController.addEventListener(joypadEventLogger);
		joypadController.connect();
	}
}

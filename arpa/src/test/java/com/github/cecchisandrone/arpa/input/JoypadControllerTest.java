package com.github.cecchisandrone.arpa.input;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.cecchisandrone.arpa.input.JoypadController;
import com.github.cecchisandrone.arpa.input.JoypadEventLogger;

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
		assertTrue(joypadController.connect());
		assertFalse(joypadController.connect());
	}

	@Test
	public void testDisconnect() throws IOException {
		joypadController.disconnect();
	}
}

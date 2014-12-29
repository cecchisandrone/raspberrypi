package it.cecchi.arpa.input;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JoypadController {

	private static final Logger LOGGER = LoggerFactory.getLogger(JoypadController.class);

	private static JoypadController joypadInterface;

	public enum Button {
		A, B, X, Y, LB, RB, BACK, START, GUIDE, TL, TR
	}

	public enum Analog {
		X1, Y1, X2, Y2, LT, RT, X3, Y3
	}

	private static Map<Button, Integer> oldButtonValues = new HashMap<Button, Integer>();

	private static Map<Analog, Integer> oldAnalogValues = new HashMap<Analog, Integer>();

	private static String device;

	private static FileInputStream deviceStream;

	private List<JoypadEventListener> listeners = new ArrayList<JoypadEventListener>();

	private JoypadController(String device) {
		JoypadController.device = device;

		// Init old values to 0 for all buttons
		for (Button button : Button.values()) {
			oldButtonValues.put(button, 0);
		}

		// Init old values to 0 for all keys
		for (Analog analog : Analog.values()) {
			oldAnalogValues.put(analog, 0);
		}
	}

	public static JoypadController getInstance(String device) throws IOException {

		if (joypadInterface == null) {
			joypadInterface = new JoypadController(device);

		}
		return joypadInterface;
	}

	public void connect() throws IOException {

		try {
			deviceStream = new FileInputStream(device);

			byte[] buf = new byte[8];
			while (deviceStream.read(buf) >= 0) {

				int newValue = -1;
				int oldValue = -1;
				Button changedButton = null;
				Analog changedAnalog = null;

				LOGGER.debug(String.format("%02X%02X %02X%02X %02X%02X %02X%02X", buf[0], buf[1], buf[2], buf[3],
						buf[4], buf[5], buf[6], buf[7]));

				if ((buf[6] & 0x80) == 0x80) {
					LOGGER.debug("Skipping configuration data");
				} else {
					// Check BYTE[6] to verify if it's a button (0x01) or analog
					// (0x02)
					if (buf[6] == 0x01) {
						// If it's a button check BYTE[4] if it's pressed or
						// released
						newValue = (int) buf[4];
						changedButton = Button.values()[(int) buf[7]];
						oldValue = oldButtonValues.get(changedButton);

						Character.forDigit((buf[4] >> 4) & 0xF, 16);
						Character.forDigit((buf[4] & 0xF), 16);

						// Update old value for button
						oldButtonValues.put(changedButton, newValue);

					} else if (buf[6] == 0x02) {

						// buf[4] LSB buf[5] MSB
						String hexString = String.format("%02X", buf[5]) + String.format("%02X", buf[4]);
						newValue = Integer.valueOf(hexString, 16);
						changedAnalog = Analog.values()[(int) buf[7]];
						oldValue = oldAnalogValues.get(changedAnalog);

						// Update old value for button
						oldAnalogValues.put(changedAnalog, newValue);
					}

					for (JoypadEventListener listener : listeners) {

						// Check if listener is interested in this button or
						// analog
						if (listener.getButtonsToNotify().contains(changedButton)) {
							listener.joypadEventTriggered(new JoypadEvent(newValue, oldValue, changedButton));
						} else if (listener.getAnalogsToNotify().contains(changedAnalog)) {
							listener.joypadEventTriggered(new JoypadEvent(newValue, oldValue, changedAnalog));
						}
					}
				}
			}
		} finally {
			deviceStream.close();
		}
	}

	public void disconnect() throws IOException {

	}

	public void addEventListener(JoypadEventListener eventListener) {
		listeners.add(eventListener);
	}

	public void removeEventListener(JoypadEventListener eventListener) {
		listeners.remove(eventListener);
	}
}

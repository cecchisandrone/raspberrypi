package com.github.cecchisandrone.raspio.gpio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servo motor driver based on ServoBlaster
 * 
 * https://github.com/richardghirst/PiBits/tree/master/ServoBlaster
 * 
 * Pin numbers are assigned using numbering on the P1 header
 * 
 * @author Alessandro
 *
 */
public class ServoMotorDevice extends AbstractDevice {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServoMotorDevice.class);

	private static final String SERVOBLASTER_FILE = "/dev/servoblaster";

	public static final int POSITION_RANGE = 100;

	private int controlPin = 0;

	private FileOutputStream deviceStream;

	private static final String command = "P1-%d=%d%%"; // P1-16=40%

	/**
	 * Pin numbers are assigned using numbering on the P1 header
	 * 
	 * @param controlPin
	 */
	public void setControlPin(int controlPin) {
		this.controlPin = controlPin;
	}

	@Override
	public void internalInit() {

		if (controlPin == 0) {
			throw new IllegalArgumentException("Not all the required pin are set");
		}

		try {
			deviceStream = new FileOutputStream(SERVOBLASTER_FILE, true);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File " + SERVOBLASTER_FILE + " not existing");
		}
	}

	/**
	 * Change the motor position 
	 * 
	 * @param position - the position value. 0 <= position <= 100
	 */
	public void changePosition(int position) {

		checkInitialized();

		if (position > POSITION_RANGE || position < 0) {
			throw new IllegalArgumentException(position + " is an illegal value for position parameter");
		}

		String str = String.format(command, controlPin, position);
		str += "\n";
		try {
			deviceStream.write(str.getBytes());
		} catch (IOException e) {
			LOGGER.error("Unable to send command " + str + " to " + SERVOBLASTER_FILE + ". Reason: " + e.toString(), e);
		}
	}

	public void close() {
		try {
			deviceStream.close();
		} catch (IOException e) {
			LOGGER.error("Unable to close device " + SERVOBLASTER_FILE);
		}
	}

	/**
	 * Expect a configuration string like this: pin
	 */
	@Override
	public void loadConfiguration(String configurationString) {

		this.controlPin = getPinByPinNumber(configurationString).getAddress();
	}
}

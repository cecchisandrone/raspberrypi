package com.github.cecchisandrone.raspio.gpio;

import com.github.cecchisandrone.raspio.gpio.SonarDevice;
import com.github.cecchisandrone.raspio.service.DeviceManager;
import com.github.cecchisandrone.raspio.service.IOServiceException;

public class DeviceManagerTest {

	public static void main(String[] args) {
		DeviceManager deviceManager = new DeviceManager();
		try {
			SonarDevice device = (SonarDevice) deviceManager.getDevice(SonarDevice.class, "1");
			System.out.println(device.getRange());
		} catch (IOServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

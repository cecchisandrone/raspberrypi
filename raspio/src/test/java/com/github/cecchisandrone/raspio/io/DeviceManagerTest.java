package com.github.cecchisandrone.raspio.io;

import org.junit.Test;

import com.github.cecchisandrone.raspio.service.DeviceManager;
import com.github.cecchisandrone.raspio.service.IOServiceException;

public class DeviceManagerTest {

	private DeviceManager deviceManager = new DeviceManager();

	@Test(expected = IOServiceException.class)
	public void testGetDevice() throws IOServiceException {
		deviceManager.getDevice(SonarDevice.class, "2");
	}

}

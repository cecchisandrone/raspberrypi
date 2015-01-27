package com.github.cecchisandrone.raspio.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.cecchisandrone.raspio.gpio.RelayDevice;
import com.github.cecchisandrone.raspio.gpio.SonarDevice;

@RestController
@RequestMapping("/rest")
public class IOService {

	private DeviceManager deviceManager = new DeviceManager();

	@RequestMapping(method = RequestMethod.GET, value = "/sonar/{id}/distance")
	public double getDistance(@PathVariable(value = "id") String id,
			@RequestParam(value = "measurements", defaultValue = "1") int measurements) throws IOServiceException {

		SonarDevice sonar = (SonarDevice) deviceManager.getDevice(SonarDevice.class, id);

		double sum = 0;
		if (measurements <= 0) {
			measurements = 1;
		}

		for (int i = 0; i < measurements; i++) {
			sum = sonar.getRange() + sum;
		}
		return sum / measurements;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/relay/{id}/toggleRelay")
	public void toggleRelay(@PathVariable(value = "id") String id, @RequestParam(value = "status") boolean status)
			throws IOServiceException {

		RelayDevice relay = (RelayDevice) deviceManager.getDevice(RelayDevice.class, id);

		// Boolean logic inverted on pin
		relay.toggleRelay(!status);
	}
}

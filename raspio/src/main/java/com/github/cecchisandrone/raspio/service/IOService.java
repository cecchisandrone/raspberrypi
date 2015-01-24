package com.github.cecchisandrone.raspio.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.github.cecchisandrone.raspio.io.RelayDevice;
import com.github.cecchisandrone.raspio.io.SonarDevice;

@Path("/io")
public class IOService {

	private DeviceManager deviceManager = new DeviceManager();

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/sonar/{id}/distance")
	public double getDistance(@PathParam(value = "id") String id, @QueryParam(value = "measurements") int measurements)
			throws IOServiceException {

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

	@POST
	@Path("/relay/{id}/toggleRelay")
	public void toggleRelay(@PathParam(value = "id") String id, @QueryParam(value = "status") boolean status)
			throws IOServiceException {

		RelayDevice relay = (RelayDevice) deviceManager.getDevice(RelayDevice.class, id);

		// Boolean logic inverted on pin
		relay.toggleRelay(!status);
	}
}

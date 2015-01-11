package it.cecchi.raspio.service;

import it.cecchi.raspio.io.Device;
import it.cecchi.raspio.io.RelayDevice;
import it.cecchi.raspio.io.SonarDevice;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/io")
public class IOService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IOService.class);

	private List<Device> deviceList = new ArrayList<Device>();

	private DeviceFactory deviceFactory = new DeviceFactory();

	public IOService() {
		InputStream is = IOService.class.getResourceAsStream("/devices.properties");
		if (is == null) {
			throw new IllegalArgumentException("devices.properties not found in the classpath");
		}
		Properties p = new Properties();
		try {
			p.load(is);
		} catch (IOException e) {
			LOGGER.error("Unable to load devices.properties", e);
		}

		for (Entry<Object, Object> entry : p.entrySet()) {
			try {
				Device device = deviceFactory.createDevice((String) entry.getKey(), (String) entry.getValue());
				deviceList.add(device);
			} catch (DeviceFactoryException e) {
				LOGGER.error("Unable to get a device", e);
			}
		}
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/sonar/{index}/distance")
	public double getDistance(@PathParam(value = "index") int index,
			@QueryParam(value = "measurements") int measurements) throws IOServiceException {

		SonarDevice sonar = (SonarDevice) getDevice(index, SonarDevice.class);

		double sum = 0;
		if (measurements <= 0) {
			measurements = 1;
		}

		for (int i = 0; i < measurements; i++) {
			sum = sonar.getRange() + sum;
		}
		return sum / measurements;
	}

	private Device getDevice(int index, Class clazz) throws IOServiceException {
		Device device = deviceList.get(index);
		try {
			return (Device) clazz.cast(device);
		} catch (ClassCastException e) {
			LOGGER.error("The device at address " + index + " is not an instance of " + clazz);
			throw new IOServiceException("The device at address " + index + " is not an instance of " + clazz, e);
		}
	}

	@POST
	@Path("/relay/{index}/toggleRelay")
	public void toggleRelay(@PathParam(value = "index") int index, @QueryParam(value = "status") boolean status)
			throws IOServiceException {

		// Boolean logic inverted on pin
		RelayDevice relay = (RelayDevice) getDevice(index, RelayDevice.class);

		relay.toggleRelay(!status);
	}
}

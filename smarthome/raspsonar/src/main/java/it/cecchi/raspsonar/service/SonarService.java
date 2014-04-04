package it.cecchi.raspsonar.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/sonarservice")
public class SonarService {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/distance")
	public double getDistance(@QueryParam(value = "measurements") int measurements) {

		double sum = 0;
		if (measurements <= 0) {
			measurements = 1;
		}

		for (int i = 0; i < measurements; i++) {
			sum = SonarSensor.getInstance().getRange() + sum;
		}
		return sum / measurements;
	}
}

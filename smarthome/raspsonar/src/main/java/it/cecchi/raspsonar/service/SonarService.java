package it.cecchi.raspsonar.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Example resource class hosted at the URI path "/sonarservice"
 */
@Path("/sonarservice")
public class SonarService {

	/**
	 * Method processing HTTP GET requests, producing "text/plain" MIME media type.
	 * 
	 * @return Double that will be send back as a response of type "text/plain".
	 * @throws Exception
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/distance")
	public double getDistance() throws Exception {

		try {
			double range = SonarSensor.getInstance().getRange();
			return range;
		} catch (UnsatisfiedLinkError e) {
			throw e;
		}
	}
}

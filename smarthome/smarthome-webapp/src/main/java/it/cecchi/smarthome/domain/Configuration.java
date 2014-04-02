package it.cecchi.smarthome.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;


public class Configuration {

	@Email
	@NotEmpty
	private String email;

	@URL
	@NotEmpty
	private String serviceUrl;

	@NotNull
	@Min(10)
	@Max(100)
	private Double distanceThreshold;

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public void setDistanceThreshold(Double distanceThreshold) {
		this.distanceThreshold = distanceThreshold;
	}


	public Double getDistanceThreshold() {
		return distanceThreshold;
	}
}

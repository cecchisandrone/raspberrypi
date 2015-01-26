package com.github.cecchisandrone.smarthome.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

@Entity
public class Configuration implements Serializable {

	private static final long serialVersionUID = -3068442304050903407L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Email
	@NotEmpty
	private String email;

	@URL
	@NotEmpty
	private String serviceUrl;

	@NotNull
	@Min(10)
	@Max(200)
	private Double distanceThreshold;

	@NotNull
	@Min(10)
	@Max(200)
	@Column(name = "AUTO_OFF_THRESHOLD")
	private Double autoPowerOffDistanceThreshold;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "CONFIGURATION_ID")
	private List<CameraConfiguration> cameraConfigurations;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public void setCameraConfigurations(List<CameraConfiguration> cameraConfigurations) {
		this.cameraConfigurations = cameraConfigurations;
	}

	public List<CameraConfiguration> getCameraConfigurations() {
		return cameraConfigurations;
	}

	public void setAutoPowerOffDistanceThreshold(Double autoPowerOffDistanceThreshold) {
		this.autoPowerOffDistanceThreshold = autoPowerOffDistanceThreshold;
	}

	public Double getAutoPowerOffDistanceThreshold() {
		return autoPowerOffDistanceThreshold;
	}
}

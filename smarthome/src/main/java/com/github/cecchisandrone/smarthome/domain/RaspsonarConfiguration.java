package com.github.cecchisandrone.smarthome.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

@Entity
public class RaspsonarConfiguration implements Serializable {

	private static final long serialVersionUID = -6035293472325449318L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

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
	
	@NotNull
	private Integer sonarIndex;
	
	@NotNull
	private Integer relayIndex;

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

	public Double getDistanceThreshold() {
		return distanceThreshold;
	}

	public void setDistanceThreshold(Double distanceThreshold) {
		this.distanceThreshold = distanceThreshold;
	}

	public Double getAutoPowerOffDistanceThreshold() {
		return autoPowerOffDistanceThreshold;
	}

	public void setAutoPowerOffDistanceThreshold(Double autoPowerOffDistanceThreshold) {
		this.autoPowerOffDistanceThreshold = autoPowerOffDistanceThreshold;
	}
	
	public void setRelayIndex(Integer relayIndex) {
		this.relayIndex = relayIndex;
	}
	
	public void setSonarIndex(Integer sonarIndex) {
		this.sonarIndex = sonarIndex;
	}
	
	public Integer getSonarIndex() {
		return sonarIndex;
	}
	
	public Integer getRelayIndex() {
		return relayIndex;
	}
}

package com.github.cecchisandrone.smarthome.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;

@Entity
public class Configuration implements Serializable {

	private static final long serialVersionUID = -3068442304050903407L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToOne(cascade = CascadeType.ALL)
	@Valid
	private RaspsonarConfiguration raspsonarConfiguration;

	@OneToOne(cascade = CascadeType.ALL)
	@Valid
	private SlackConfiguration slackConfiguration;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "CONFIGURATION_ID")
	private List<CameraConfiguration> cameraConfigurations;

	@OneToOne(cascade = CascadeType.ALL)
	@Valid
	private ZoneMinderConfiguration zoneMinderConfiguration;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setCameraConfigurations(List<CameraConfiguration> cameraConfigurations) {
		this.cameraConfigurations = cameraConfigurations;
	}

	public List<CameraConfiguration> getCameraConfigurations() {
		return cameraConfigurations;
	}

	public void setZoneMinderConfiguration(ZoneMinderConfiguration zoneMinderConfiguration) {
		this.zoneMinderConfiguration = zoneMinderConfiguration;
	}

	public ZoneMinderConfiguration getZoneMinderConfiguration() {
		return zoneMinderConfiguration;
	}

	public RaspsonarConfiguration getRaspsonarConfiguration() {
		return raspsonarConfiguration;
	}

	public void setRaspsonarConfiguration(RaspsonarConfiguration raspsonarConfiguration) {
		this.raspsonarConfiguration = raspsonarConfiguration;
	}

	public SlackConfiguration getSlackConfiguration() {
		return slackConfiguration;
	}

	public void setSlackConfiguration(SlackConfiguration slackConfiguration) {
		this.slackConfiguration = slackConfiguration;
	}

}

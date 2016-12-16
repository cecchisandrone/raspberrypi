package com.github.cecchisandrone.smarthome.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

@Entity
public class ZoneMinderConfiguration implements Serializable {

	private static final long serialVersionUID = -3068442304050903407L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private Boolean automaticActivationEnabled;

	private String zmHost;

	@Pattern(message = "Should be a valid MAC address", regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")
	private String zmHostMacAddress;

	private String zmHostUser;

	private String zmHostPassword;

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getZmHostUser() {
		return zmHostUser;
	}

	public void setZmHostUser(String zmHostUser) {
		this.zmHostUser = zmHostUser;
	}

	public String getZmHostPassword() {
		return zmHostPassword;
	}

	public void setZmHostPassword(String zmHostPassword) {
		this.zmHostPassword = zmHostPassword;
	}

	public Boolean getAutomaticActivationEnabled() {
		return automaticActivationEnabled;
	}

	public void setAutomaticActivationEnabled(Boolean automaticActivationEnabled) {
		this.automaticActivationEnabled = automaticActivationEnabled;
	}

	public String getZmHost() {
		return zmHost;
	}

	public void setZmHost(String zmHost) {
		this.zmHost = zmHost;
	}

	public String getZmHostMacAddress() {
		return zmHostMacAddress;
	}

	public void setZmHostMacAddress(String zmHostMacAddress) {
		this.zmHostMacAddress = zmHostMacAddress;
	}
}

package com.github.cecchisandrone.smarthome.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class TemperatureSensorConfiguration implements Serializable {

	private static final long serialVersionUID = -5924256393652824591L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotEmpty
	private String serviceHost;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getServiceHost() {
		return serviceHost;
	}

	public void setServiceHost(String serviceHost) {
		this.serviceHost = serviceHost;
	}
}

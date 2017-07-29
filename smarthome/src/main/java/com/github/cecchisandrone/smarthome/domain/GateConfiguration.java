package com.github.cecchisandrone.smarthome.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class GateConfiguration implements Serializable {

	private static final long serialVersionUID = -5924256393652824591L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotEmpty
	private String serviceHost;

	@NotNull
	private Float duration;

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

	public Float getDuration() {
		return duration;
	}

	public void setDuration(Float duration) {
		this.duration = duration;
	}
}

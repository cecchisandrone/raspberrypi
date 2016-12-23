package com.github.cecchisandrone.smarthome.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

@Entity
public class SlackConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String token;

	private String notificationChannel;

	private String locationChangeChannel;

	@Pattern(message = "Should be a list of strings separated by ;", regexp = "[-_0-9a-zA-Z]+(;[-_0-9a-zA-Z]+)*")
	private String locationChangeUsers;

	public void setNotificationChannel(String channel) {
		this.notificationChannel = channel;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNotificationChannel() {
		return notificationChannel;
	}

	public void setLocationChangeChannel(String locationChangeChannel) {
		this.locationChangeChannel = locationChangeChannel;
	}

	public String getLocationChangeChannel() {
		return locationChangeChannel;
	}

	public void setLocationChangeUsers(String locationChangeUsers) {
		this.locationChangeUsers = locationChangeUsers;
	}

	public String getLocationChangeUsers() {
		return locationChangeUsers;
	}

	public String[] getUsers() {
		return getLocationChangeUsers().split(";");
	}
}

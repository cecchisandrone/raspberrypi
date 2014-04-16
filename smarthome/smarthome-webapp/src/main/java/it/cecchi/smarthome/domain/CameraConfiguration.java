package it.cecchi.smarthome.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

@Entity
public class CameraConfiguration implements Serializable {

	private static final long serialVersionUID = 1980887710668055069L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@URL
	@NotEmpty
	private String host;

	@NotEmpty
	private String name;

	private boolean alarmEnabled;

	private String username;

	private String password;

	private boolean enabled;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isAlarmEnabled() {
		return alarmEnabled;
	}

	public void setAlarmEnabled(boolean alarmEnabled) {
		this.alarmEnabled = alarmEnabled;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

package it.cecchi.smarthome.domain;

import java.util.Date;


public class Task {

	private String text;

	private String title;

	private long id;

	private Date dueTo;

	public Task(int id, String text, String title, Date dueTo) {

		this.id = id;
		this.text = text;
		this.title = title;
		this.dueTo = dueTo;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Date getDueTo() {
		return dueTo;
	}


	public void setDueTo(Date dueTo) {
		this.dueTo = dueTo;
	}
}

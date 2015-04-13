package com.github.cecchisandrone.vc.wit;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Outcome {

	@JsonProperty("_text")
	private String text;
	
	private String intent;
	
	private Map<String, Object> entities;
	
	private float confidence;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public Map<String, Object> getEntities() {
		return entities;
	}

	public void setEntities(Map<String, Object> entities) {
		this.entities = entities;
	}

	public float getConfidence() {
		return confidence;
	}

	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}
}

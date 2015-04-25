package com.github.cecchisandrone.vc.wit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Outcome {

	@JsonProperty("_text")
	private String text;

	private String intent;

	private Entities entities;

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

	public float getConfidence() {
		return confidence;
	}

	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}

	public void setEntities(Entities entities) {
		this.entities = entities;
	}

	public Entities getEntities() {
		return entities;
	}
}

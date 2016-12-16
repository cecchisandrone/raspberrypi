package com.github.cecchisandrone.smarthome.client.slack;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Attachment {

	private String title;

	@JsonProperty("title_link")
	private String titleLink;

	@JsonProperty("thumb_url")
	private String thumbUrl;

	@JsonProperty("thumb_width")
	private Integer thumbWidth;

	@JsonProperty("thumb_height")
	private Integer thumbHeight;

	private String text;

	private String fallback;

	@JsonProperty("mrkdwn_in")
	private List<String> mrkdwnIn = null;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleLink() {
		return titleLink;
	}

	public void setTitleLink(String titleLink) {
		this.titleLink = titleLink;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	public Integer getThumbWidth() {
		return thumbWidth;
	}

	public void setThumbWidth(Integer thumbWidth) {
		this.thumbWidth = thumbWidth;
	}

	public Integer getThumbHeight() {
		return thumbHeight;
	}

	public void setThumbHeight(Integer thumbHeight) {
		this.thumbHeight = thumbHeight;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFallback() {
		return fallback;
	}

	public void setFallback(String fallback) {
		this.fallback = fallback;
	}

	public List<String> getMrkdwnIn() {
		return mrkdwnIn;
	}

	public void setMrkdwnIn(List<String> mrkdwnIn) {
		this.mrkdwnIn = mrkdwnIn;
	}
}
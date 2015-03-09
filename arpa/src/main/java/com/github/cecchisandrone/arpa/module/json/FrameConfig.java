package com.github.cecchisandrone.arpa.module.json;

public class FrameConfig {

	private int width;

	private int height;

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	@Override
	public String toString() {
		return width + "x" + height;
	}
}

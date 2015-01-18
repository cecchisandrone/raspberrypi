package com.github.cecchisandrone.arpa.input;

import com.github.cecchisandrone.arpa.input.JoypadController.Analog;
import com.github.cecchisandrone.arpa.input.JoypadController.Button;

public class JoypadEvent {

	private int newValue;

	private int oldValue;

	private Button changedButton;

	private Analog changedAnalog;

	public JoypadEvent(int newValue, int oldValue, Button changedButton) {
		this.newValue = newValue;
		this.oldValue = oldValue;
		this.changedButton = changedButton;
	}

	public JoypadEvent(int newValue, int oldValue, Analog changedAnalog) {
		this.newValue = newValue;
		this.oldValue = oldValue;
		this.changedAnalog = changedAnalog;
	}

	public int getNewValue() {
		return newValue;
	}

	public int getOldValue() {
		return oldValue;
	}

	public Button getChangedButton() {
		return changedButton;
	}

	public Analog getChangedAnalog() {
		return changedAnalog;
	}
}

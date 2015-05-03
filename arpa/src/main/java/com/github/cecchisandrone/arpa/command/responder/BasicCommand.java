package com.github.cecchisandrone.arpa.command.responder;

import com.github.cecchisandrone.arpa.command.Command;
import com.github.cecchisandrone.arpa.util.LocalizedPicoTextToSpeechWrapper;
import com.github.cecchisandrone.vc.wit.Outcome;

public class BasicCommand implements Command {

	private static final String COMMAND_PREFIX = "commands.";

	protected LocalizedPicoTextToSpeechWrapper localizedPicoTextToSpeechWrapper;

	public void setLocalizedPicoTextToSpeechWrapper(LocalizedPicoTextToSpeechWrapper localizedPicoTextToSpeechWrapper) {
		this.localizedPicoTextToSpeechWrapper = localizedPicoTextToSpeechWrapper;
	}

	@Override
	public void execute(Outcome outcome) {

		localizedPicoTextToSpeechWrapper.playMessage(COMMAND_PREFIX + outcome.getIntent(), resolveArguments(outcome));
	}

	protected String[] resolveArguments(Outcome outcome) {
		return new String[] {};
	}
}

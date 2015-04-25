package com.github.cecchisandrone.arpa.command.responder;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.github.cecchisandrone.arpa.command.Command;
import com.github.cecchisandrone.vc.audio.PicoTextToSpeechWrapper;
import com.github.cecchisandrone.vc.wit.Outcome;

public abstract class AbstractResponderCommand implements Command {

	private static final String COMMAND_PREFIX = "commands.";

	private PicoTextToSpeechWrapper picoTextToSpeechWrapper;

	private MessageSource messageSource;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setPicoTextToSpeechWrapper(PicoTextToSpeechWrapper picoTextToSpeechWrapper) {
		this.picoTextToSpeechWrapper = picoTextToSpeechWrapper;
	}

	@Override
	public void execute(Outcome outcome) {

		String message = messageSource.getMessage(COMMAND_PREFIX + outcome.getIntent(), resolveArguments(),
				LocaleContextHolder.getLocale());
		picoTextToSpeechWrapper.playMessage(message);
	}

	public abstract String[] resolveArguments();
}

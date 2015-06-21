package com.github.cecchisandrone.arpa.util;

import org.springframework.context.MessageSource;

import com.github.cecchisandrone.vc.audio.PicoTextToSpeechWrapper;

public class LocalizedPicoTextToSpeechWrapper extends PicoTextToSpeechWrapper {

	private MessageSource messageSource;

	public LocalizedPicoTextToSpeechWrapper(Language language, String file) {
		super(language, file);
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public void playMessage(String messageKey) {

		String message = messageSource.getMessage(messageKey, null, getLanguage().getLocale());
		super.playMessage(message);
	}

	public void playMessage(String messageKey, String[] args) {

		String message = messageSource.getMessage(messageKey, args, getLanguage().getLocale());
		super.playMessage(message);
	}

}

package com.github.cecchisandrone.arpa.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

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

		String message = messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
		super.playMessage(message);
	}

	public void playMessage(String messageKey, String[] args) {

		System.out.println(LocaleContextHolder.getLocale());
		String message = messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
		super.playMessage(message);
	}

}

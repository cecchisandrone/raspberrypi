package com.github.cecchisandrone.arpa.command;

import java.util.List;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

import com.github.cecchisandrone.arpa.util.LocalizedPicoTextToSpeechWrapper;
import com.github.cecchisandrone.vc.wit.Language;
import com.github.cecchisandrone.vc.wit.Outcome;

public class ChangeLocaleCommand implements Command {

	private LocalizedPicoTextToSpeechWrapper localizedPicoTextToSpeechWrapper;

	public void setLocalizedPicoTextToSpeechWrapper(LocalizedPicoTextToSpeechWrapper localizedPicoTextToSpeechWrapper) {
		this.localizedPicoTextToSpeechWrapper = localizedPicoTextToSpeechWrapper;
	}

	@Override
	public void execute(Outcome outcome) {

		List<Language> languages = outcome.getEntities().getLanguages();
		if (languages != null && languages.size() != 0) {
			Language language = languages.get(0);
			String languageString = language.getMetadata();
			String[] split = languageString.split("_");
			Locale locale = new Locale(split[0], split[1]);
			LocaleContextHolder.setLocale(locale);
			localizedPicoTextToSpeechWrapper
					.setLanguage(com.github.cecchisandrone.vc.audio.PicoTextToSpeechWrapper.Language
							.valueOf(languageString.toUpperCase()));
			localizedPicoTextToSpeechWrapper.playMessage("language.changed",
					new String[] { locale.getDisplayLanguage(locale) });
		} else {
			localizedPicoTextToSpeechWrapper.playMessage("language.specify_language");
		}
	}
}

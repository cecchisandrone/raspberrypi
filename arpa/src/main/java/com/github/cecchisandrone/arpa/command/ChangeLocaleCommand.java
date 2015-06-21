package com.github.cecchisandrone.arpa.command;

import java.util.List;
import java.util.Locale;

import com.github.cecchisandrone.arpa.command.responder.BasicCommand;
import com.github.cecchisandrone.vc.wit.Language;
import com.github.cecchisandrone.vc.wit.Outcome;

public class ChangeLocaleCommand extends BasicCommand {

	@Override
	public void execute(Outcome outcome) {

		List<Language> languages = outcome.getEntities().getLanguages();
		if (languages != null && languages.size() != 0) {
			Language language = languages.get(0);
			String languageString = language.getMetadata();
			String[] split = languageString.split("_");
			Locale locale = new Locale(split[0], split[1]);
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

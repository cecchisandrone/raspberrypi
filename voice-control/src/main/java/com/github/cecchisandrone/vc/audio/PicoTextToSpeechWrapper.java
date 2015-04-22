package com.github.cecchisandrone.vc.audio;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PicoTextToSpeechWrapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(PicoTextToSpeechWrapper.class);

	public enum Language {
		EN_US("en-US"), EN_GB("en-GB"), DE_DE("de-DE"), ES_ES("es-ES"), FR_FR("fr-FR"), IT_IT("it-IT");

		private String language;

		Language(String language) {
			this.language = language;
		}

		public String getLanguage() {
			return language;
		}
	}

	private Language language;

	private String file;

	public PicoTextToSpeechWrapper(Language language, String file) {
		this.language = language;
		this.file = file;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void playMessage(String message) {

		try {
			ProcessBuilder pb = new ProcessBuilder();
			pb.command("pico2wave", "--lang", language.getLanguage(), "-w", file, message);
			Process process = pb.start();
			process.waitFor();
			pb.command("aplay", "-D", "hw:1,0", file);
			process = pb.start();
			process.waitFor();

		} catch (IOException e) {
			LOGGER.error(e.toString(), e);
		} catch (InterruptedException e) {
			LOGGER.error(e.toString(), e);
		}
	}
}

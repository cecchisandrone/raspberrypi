package com.github.cecchisandrone.vc.audio;

import com.github.cecchisandrone.vc.audio.PicoTextToSpeechWrapper.Language;

public class PicoTextToSpeechWrapperTest {

	public static void main(String[] args) {

		PicoTextToSpeechWrapper picoTextToSpeechWrapper = new PicoTextToSpeechWrapper(Language.IT_IT, "/tmp/speech.wav");
		picoTextToSpeechWrapper.playMessage("Questo è un messaggio di prova");
	}
}

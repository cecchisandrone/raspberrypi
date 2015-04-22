package com.github.cecchisandrone.arpa.module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.github.cecchisandrone.raspio.input.JoypadController;
import com.github.cecchisandrone.raspio.input.JoypadController.Analog;
import com.github.cecchisandrone.raspio.input.JoypadController.Button;
import com.github.cecchisandrone.raspio.input.JoypadEvent;
import com.github.cecchisandrone.raspio.input.JoypadEventListener;
import com.github.cecchisandrone.vc.audio.PicoTextToSpeechWrapper;
import com.github.cecchisandrone.vc.audio.Player;
import com.github.cecchisandrone.vc.wit.WitClient;
import com.github.cecchisandrone.vc.wit.WitResponse;

public class VoiceControlModule extends AbstractAgentModule implements JoypadEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(VoiceControlModule.class);

	@Autowired
	private JoypadController joypadController;

	private Player player = new Player();

	private WitResponse witResponse;

	@Autowired
	private WitClient witClient;

	@Autowired
	private PicoTextToSpeechWrapper picoTextToSpeechWrapper;

	@Override
	protected void executeWork() {
		if (witResponse != null) {
			picoTextToSpeechWrapper.playMessage("Processo intenti");
			System.out.println("Processing intent: " + witResponse);
			witResponse = null;
		}
	}

	@Override
	public void init() {

		super.init();

		// Joypad initialization
		joypadController.addEventListener(this);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				joypadController.disconnect();
			}
		});
		joypadController.connect();
		try {
			// Send a dummy request to initialize SSL
			witClient.sendAudio(new ClassPathResource("/sounds/start.wav").getFile());
		} catch (IOException e) {
			LOGGER.error(e.toString(), e);
		}
	}

	public List<Button> getButtonsToNotify() {
		return Arrays.asList(Button.LB);
	}

	public List<Analog> getAnalogsToNotify() {
		return new ArrayList<JoypadController.Analog>();
	}

	@Override
	public void joypadEventTriggered(JoypadEvent event) {
		if (event.getChangedButton() != null && event.getChangedButton().equals(Button.LB) && event.getNewValue() == 1) {
			try {
				player.play(new ClassPathResource("/sounds/start.wav").getFile());
				witResponse = witClient.sendChunkedAudio();
			} catch (IOException e) {
				LOGGER.error(e.toString(), e);
			}
		}
	}
}

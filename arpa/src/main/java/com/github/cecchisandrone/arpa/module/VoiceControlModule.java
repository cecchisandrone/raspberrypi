package com.github.cecchisandrone.arpa.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.cecchisandrone.arpa.command.Command;
import com.github.cecchisandrone.arpa.command.CommandFactory;
import com.github.cecchisandrone.arpa.command.CommandNotFoundException;
import com.github.cecchisandrone.arpa.util.LocalizedPicoTextToSpeechWrapper;
import com.github.cecchisandrone.arpa.util.Resources;
import com.github.cecchisandrone.raspio.input.JoypadController;
import com.github.cecchisandrone.raspio.input.JoypadController.Analog;
import com.github.cecchisandrone.raspio.input.JoypadController.Button;
import com.github.cecchisandrone.raspio.input.JoypadEvent;
import com.github.cecchisandrone.raspio.input.JoypadEventListener;
import com.github.cecchisandrone.vc.audio.Player;
import com.github.cecchisandrone.vc.wit.Outcome;
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
	private LocalizedPicoTextToSpeechWrapper localizedPicoTextToSpeechWrapper;

	@Autowired
	private CommandFactory commandFactory;

	private float confidenceThreshold;

	public void setConfidenceThreshold(float confidenceThreshold) {
		this.confidenceThreshold = confidenceThreshold;
	}

	@Override
	protected void executeWork() {

		if (witResponse != null) {

			if (witResponse.getOutcomes().length != 0) {
				player.play(Resources.getFile(Resources.VOICE_CONTROL_OK));
				for (Outcome outcome : witResponse.getOutcomes()) {
					if (outcome.getConfidence() > confidenceThreshold) {
						try {
							Command command = commandFactory.getCommand(outcome.getIntent());
							command.execute(outcome);
						} catch (CommandNotFoundException e) {
							LOGGER.error(e.toString(), e);
							localizedPicoTextToSpeechWrapper.playMessage("system.command_not_found");
						}
					} else {
						localizedPicoTextToSpeechWrapper.playMessage("system.command_not_found");
					}
				}
			} else {
				player.play(Resources.getFile(Resources.VOICE_CONTROL_NO));
			}
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

		// Send a dummy request to initialize SSL
		witClient.sendAudio(Resources.getFile(Resources.DUMMY_SOUND));
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
			player.play(Resources.getFile(Resources.VOICE_CONTROL_START));
			witResponse = witClient.sendChunkedAudio();
		}
	}
}

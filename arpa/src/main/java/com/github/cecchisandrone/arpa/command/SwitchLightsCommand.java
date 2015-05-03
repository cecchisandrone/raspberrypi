package com.github.cecchisandrone.arpa.command;

import com.github.cecchisandrone.arpa.command.responder.BasicCommand;
import com.github.cecchisandrone.arpa.module.SystemModule;
import com.github.cecchisandrone.vc.wit.Outcome;

public class SwitchLightsCommand extends BasicCommand {

	private SystemModule systemModule;

	public void setSystemModule(SystemModule systemModule) {
		this.systemModule = systemModule;
	}

	@Override
	public void execute(Outcome outcome) {
		Boolean status = systemModule.switchLights();
		if (status) {
			localizedPicoTextToSpeechWrapper.playMessage("lights.on");
		} else {
			localizedPicoTextToSpeechWrapper.playMessage("lights.off");
		}
	}
}

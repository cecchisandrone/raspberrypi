package com.github.cecchisandrone.arpa.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.cecchisandrone.raspio.input.JoypadController;

public class NavigationModule extends AbstractAgentModule {

	@Autowired
	private JoypadController joypadController;

	private static final Logger LOGGER = LoggerFactory.getLogger(NavigationModule.class);

	@Override
	public void executeWork() {
		LOGGER.info("Executing NavigationModule");
	}
}

package com.github.cecchisandrone.arpa.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutonomousNavigationModule extends NavigationModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutonomousNavigationModule.class);

	@Override
	public void executeWork() {
		LOGGER.info("Executing AutonomousNavigationModule");
	}
}

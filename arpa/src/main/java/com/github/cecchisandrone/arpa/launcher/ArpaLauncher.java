package com.github.cecchisandrone.arpa.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.cecchisandrone.arpa.module.ModuleContainer;

public class ArpaLauncher {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArpaLauncher.class);

	public static void main(String[] args) {

		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"application-context.xml");
		applicationContext.registerShutdownHook();

		ModuleContainer moduleContainer = applicationContext.getBean(ModuleContainer.class);
		moduleContainer.initializeModules();

		LOGGER.info("ARPA initialization completed...");
	}
}

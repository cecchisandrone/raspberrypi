package com.github.cecchisandrone.arpa.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.cecchisandrone.arpa.module.ModuleContainer;
import com.github.cecchisandrone.arpa.util.LocalizedPicoTextToSpeechWrapper;

public class ArpaLauncher {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArpaLauncher.class);

	public static void main(String[] args) {

		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"application-context.xml");
		applicationContext.registerShutdownHook();

		// Init locale
		LocalizedPicoTextToSpeechWrapper tts = applicationContext.getBean(LocalizedPicoTextToSpeechWrapper.class);
		ModuleContainer moduleContainer = applicationContext.getBean(ModuleContainer.class);
		moduleContainer.initializeModules();

		LOGGER.info("ARPA initialization completed...");

		tts.playMessage("system.ready");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				LOGGER.info("ARPA shutdown completed...");
			}
		});
	}
}

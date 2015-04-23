package com.github.cecchisandrone.arpa.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public final class Resources {

	private static final Logger LOGGER = LoggerFactory.getLogger(Resources.class);

	public static final String VOICE_CONTROL_START = "/sounds/start.wav";

	public static final String VOICE_CONTROL_OK = "/sounds/ok.wav";

	public static final String VOICE_CONTROL_NO = "/sounds/no.wav";

	public static final String DUMMY_SOUND = "/sounds/dummy.wav";

	public static final File getFile(String resource) {
		try {
			return new ClassPathResource(resource).getFile();
		} catch (IOException e) {
			LOGGER.error(e.toString(), e);
			throw new RuntimeException(e);
		}
	}
}

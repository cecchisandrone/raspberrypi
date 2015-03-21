package com.github.cecchisandrone.vc.audio;

import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Microphone {

	private static final Logger LOGGER = LoggerFactory.getLogger(Microphone.class);

	// format of audio file
	private static final AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

	// the line from which audio data is captured
	private TargetDataLine line;

	private AudioFormat audioFormat;

	public Microphone(AudioFormat audioFormat) {
		this.audioFormat = audioFormat;
	}

	/**
	 * Prepare the line for recording
	 * 
	 * @return
	 */
	public boolean open() {
		try {
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

			// Checks if system supports the data line
			if (!AudioSystem.isLineSupported(info)) {
				LOGGER.error("Line is not supported");
				System.exit(0);
			}
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(audioFormat);

		} catch (LineUnavailableException ex) {
			LOGGER.error(ex.toString(), ex);
			return false;
		}
		return true;
	}

	/**
	 * Captures the sound and return the stream
	 */
	public InputStream start() {

		if (line != null) {

			line.start(); // start capturing

			LOGGER.info("Start recording...");

			return new AudioInputStream(line);

			// AudioSystem.write(ais, fileType, outputStream);

		} else {
			throw new IllegalStateException("Line has not created. Cannot start recording");
		}
	}

	/**
	 * Stops the recording process
	 */
	public void stop() {
		line.stop();
		LOGGER.info("Stop recording...");
	}

	/**
	 * Closes the target data line to finish capturing and recording	 * 
	 */
	public void close() {
		line.stop();
		line.close();
		LOGGER.info("Data line closed");
	}
}

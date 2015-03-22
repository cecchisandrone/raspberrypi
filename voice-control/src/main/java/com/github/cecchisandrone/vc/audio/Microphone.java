package com.github.cecchisandrone.vc.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.TargetDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Microphone {

	private static final Logger LOGGER = LoggerFactory.getLogger(Microphone.class);

	private int deviceIndex;

	// the line from which audio data is captured
	private TargetDataLine line;

	private AudioFormat audioFormat;

	public Microphone(AudioFormat audioFormat, int deviceIndex) {
		this.audioFormat = audioFormat;
		this.deviceIndex = deviceIndex;
	}

	/**
	 * Prepare the line for recording
	 * 
	 * @return
	 */
	public boolean open() {
		try {
			Info info = AudioSystem.getMixerInfo()[deviceIndex];
			line = (TargetDataLine) AudioSystem.getTargetDataLine(audioFormat, info);
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
	public MicrophoneInputStream start() {

		if (line != null) {

			line.start(); // start capturing

			LOGGER.info("Start recording...");

			return new MicrophoneInputStream(new AudioInputStream(line));

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

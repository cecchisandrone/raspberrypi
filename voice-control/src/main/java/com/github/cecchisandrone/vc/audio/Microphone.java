package com.github.cecchisandrone.vc.audio;

import java.io.IOException;
import java.util.Date;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.TargetDataLine;

import junit.framework.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Microphone {

	private static final Logger LOGGER = LoggerFactory.getLogger(Microphone.class);

	private int deviceIndex;

	// the line from which audio data is captured
	private TargetDataLine line;

	private AudioFormat audioFormat;

	public AudioFormat getAudioFormat() {
		return audioFormat;
	}

	public Microphone(AudioFormat audioFormat, int deviceIndex) {
		this.audioFormat = audioFormat;
		this.deviceIndex = deviceIndex;
	}

	/**
	 * Prepare the line for recording
	 * 
	 * @return
	 */
	public void open(LineListener listener) {
		try {
			Info info = AudioSystem.getMixerInfo()[deviceIndex];
			line = (TargetDataLine) AudioSystem.getTargetDataLine(audioFormat, info);
			line.addLineListener(listener);
			line.open(audioFormat);			

		} catch (LineUnavailableException ex) {
			LOGGER.error(ex.toString(), ex);
		}		
	}

	/**
	 * Captures the sound and return the stream
	 * @throws IOException 
	 */
	public MicrophoneInputStream start() {

		Assert.assertNotNull(line);

		AudioInputStream audioInputStream = new AudioInputStream(line);
		line.start(); // start capturing
		return new MicrophoneInputStream(audioInputStream);	
	}

	/**
	 * Stops the recording process
	 */
	public void stop() {
		line.stop();
		LOGGER.info("Stop recording..." + new Date());
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

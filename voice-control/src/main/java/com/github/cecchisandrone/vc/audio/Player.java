package com.github.cecchisandrone.vc.audio;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player implements LineListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(Microphone.class);

	private Clip clip;

	private CountDownLatch latch;

	public void play(File f) {

		latch = new CountDownLatch(1);
		try {
			Line.Info linfo = new Line.Info(Clip.class);
			Line line = AudioSystem.getLine(linfo);
			clip = (Clip) line;
			clip.addLineListener(this);
			AudioInputStream ais = AudioSystem.getAudioInputStream(f);
			clip.open(ais);
			clip.start();
			latch.await();
		} catch (LineUnavailableException e) {
			LOGGER.error(e.toString(), e);
		} catch (UnsupportedAudioFileException e) {
			LOGGER.error(e.toString(), e);
		} catch (IOException e) {
			LOGGER.error(e.toString(), e);
		} catch (InterruptedException e) {
			LOGGER.error(e.toString(), e);
		}
	}

	@Override
	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();
		if (type == LineEvent.Type.STOP) {
			clip.close();
			latch.countDown();
		}
	}
}

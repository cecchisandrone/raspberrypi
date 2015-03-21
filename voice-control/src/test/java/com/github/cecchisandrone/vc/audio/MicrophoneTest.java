package com.github.cecchisandrone.vc.audio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.sound.sampled.AudioFormat;

import com.github.cecchisandrone.vc.wit.WitClient;

public class MicrophoneTest {

	// record duration, in milliseconds
	static final long RECORD_TIME = 5000;

	private static AudioFormat audioFormat = new AudioFormat(48000, 16, 1, true, false);

	/**
	 * Entry to run the program
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws URISyntaxException {
		final Microphone recorder = new Microphone(audioFormat);
		recorder.open();

		WitClient witClient = new WitClient("https://api.wit.ai/speech", audioFormat);

		for (int i = 0; i < 1; i++) {

			// creates a new thread that waits for a specified
			// of time before stopping
			Thread stopper = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(RECORD_TIME);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					recorder.stop();
				}
			});

			stopper.start();

			// start recording
			InputStream inputStream = recorder.start();
			// witClient.sendChunkedAudio(inputStream);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			byte[] buffer = new byte[1000];
			try {
				int read = -1;
				while ((read = inputStream.read(buffer)) != -1) {
					System.out.println(read);
					outputStream.write(buffer);
					witClient.sendChunkedAudio(new Bytearra);
				}
				System.out.println("Closed correctly");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		recorder.close();
	}
}

package com.github.cecchisandrone.vc.audio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

import com.github.cecchisandrone.vc.wit.WitClient;

public class MicrophoneTest {

	private static AudioFormat audioFormat = new AudioFormat(32000, 8, 1, true, false);

	private static Microphone recorder;

	/**
	 * Entry to run the program
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		int i = 0;
		for (Mixer.Info info : AudioSystem.getMixerInfo()) {
			System.out.println("[" + i++ + "] " + info);
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String indexString;
		if (args == null || args.length == 0) {
			System.out.print("Please enter the line number: ");			
			indexString = br.readLine();
		} else {
			indexString = args[0];
		}

		recorder = new Microphone(audioFormat, Integer.parseInt(indexString));
		WitClient witClient = new WitClient("https://api.wit.ai/speech", recorder);
		String json = witClient.sendAudio("src/test/resources/audio.wav");
		System.out.println(json);
		for (int j = 0; j < 3; j++) {
			json = witClient.sendChunkedAudio();
			System.out.println(json);
		}		
		witClient.close();
		recorder.close();
	}
}

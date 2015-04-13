package com.github.cecchisandrone.vc.audio;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

import com.github.cecchisandrone.vc.wit.Outcome;
import com.github.cecchisandrone.vc.wit.WitClient;
import com.github.cecchisandrone.vc.wit.WitResponse;

public class MicrophoneTest {

	private static AudioFormat audioFormat = new AudioFormat(32000, 8, 1, true, false);

	private static Microphone recorder;
	
	private static WitClient witClient;
	
	private int deviceIndex = 3;

	@Before
	public void setup() throws Exception {
		recorder = new Microphone(audioFormat, deviceIndex);
		witClient = new WitClient("https://api.wit.ai/speech", recorder);
	}
	
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
		witClient.setMaxRecordLength(10000);
		WitResponse witResponse = witClient.sendChunkedAudio();
		System.out.println(witResponse);		
		witResponse = witClient.sendAudio("src/test/resources/hello.wav");
		System.out.println(witResponse);
		witClient.close();
		recorder.close();
	}
	
	@Test
	public void testSendAudioReturnsIntentWithEntity() {
		WitResponse witResponse = witClient.sendAudio("src/test/resources/get_name.wav");
		Assert.assertThat(witResponse.getText(), is("come si chiama mia moglie"));
		Assert.assertThat(witResponse.getOutcomes().length, is(1));
		Outcome o = witResponse.getOutcomes()[0];
		Assert.assertThat(o.getIntent(), is("get_name"));
		Assert.assertThat(o.getText(), is("come si chiama mia moglie"));
		Assert.assertThat(o.getEntities().get("contact"), notNullValue());
	}
}

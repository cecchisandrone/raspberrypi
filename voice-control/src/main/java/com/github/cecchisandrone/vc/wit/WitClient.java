package com.github.cecchisandrone.vc.wit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cecchisandrone.vc.audio.Microphone;
import com.github.cecchisandrone.vc.audio.MicrophoneInputStream;

public class WitClient implements LineListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(WitClient.class);

	// Maximum record duration, in milliseconds
	private long maxRecordLength = 5000;

	private String baseUri;

	private Microphone microphone;

	private Thread stopperThread;

	private CloseableHttpClient httpclient = HttpClients.createDefault();

	private ObjectMapper objectMapper = new ObjectMapper();

	private String serverToken;

	private long startTimestamp;

	public WitClient(String url, String serverToken, Microphone microphone) throws Exception {
		URIBuilder builder = new URIBuilder(url);
		builder.addParameter("v", "20150318");
		baseUri = builder.toString();
		this.microphone = microphone;
		this.serverToken = serverToken;
	}

	public void setServerToken(String serverToken) {
		this.serverToken = serverToken;
	}

	/**
	 * Sets the maximum record length in ms. After this length the recording will be stopped.
	 * Default is 5000 ms
	 * 
	 * @param maxRecordLength
	 */
	public void setMaxRecordLength(long maxRecordLength) {
		this.maxRecordLength = maxRecordLength;
	}

	public WitResponse sendChunkedAudio() {

		HttpPost httpPost;
		try {

			long time = new Date().getTime();

			String encoding = microphone.getAudioFormat().getEncoding() == Encoding.PCM_SIGNED ? "signed-integer"
					: "unsigned-integer";
			String bits = Integer.toString(microphone.getAudioFormat().getSampleSizeInBits());
			String rate = Long.toString((long) microphone.getAudioFormat().getSampleRate());
			String endian = microphone.getAudioFormat().isBigEndian() ? "big" : "little";

			microphone.open(this);
			MicrophoneInputStream inputStream = microphone.start();
			httpPost = new HttpPost(baseUri);
			InputStreamEntity reqEntity = new InputStreamEntity(inputStream, -1);
			reqEntity.setChunked(true);
			httpPost.setEntity(reqEntity);
			httpPost.addHeader(new BasicHeader("Authorization", "Bearer " + serverToken));
			httpPost.addHeader("Content-Type", "audio/raw;encoding=" + encoding + ";bits=" + bits + ";rate=" + rate
					+ ";endian=" + endian + ";");

			startTimestamp = new Date().getTime();
			CloseableHttpResponse response = httpclient.execute(httpPost);
			stopperThread.interrupt();

			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			// System.out.println(IOUtils.toString(content));
			System.out.println("Time taken: " + (new Date().getTime() - time));
			WitResponse value = objectMapper.readValue(content, WitResponse.class);
			EntityUtils.consume(entity);
			httpPost.releaseConnection();
			return value;

		} catch (ClientProtocolException e) {
			LOGGER.error(e.toString(), e);
		} catch (IOException e) {
			LOGGER.error(e.toString(), e);
		}
		return null;
	}

	public WitResponse sendAudio(File file) {
		HttpPost httpPost;
		try {
			long time = new Date().getTime();
			httpPost = new HttpPost(baseUri);
			InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), file.length(),
					ContentType.create("audio/wav"));

			reqEntity.setChunked(true);
			httpPost.setEntity(reqEntity);
			httpPost.addHeader(new BasicHeader("Authorization", "Bearer " + serverToken));
			httpPost.addHeader("Content-Type", "audio/wav");
			System.out.println("Executing request: " + httpPost.getRequestLine());

			CloseableHttpResponse response = httpclient.execute(httpPost);

			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			System.out.println("Time taken: " + (new Date().getTime() - time));
			WitResponse value = objectMapper.readValue(content, WitResponse.class);
			EntityUtils.consume(entity);
			httpPost.releaseConnection();
			return value;
		} catch (ClientProtocolException e) {
			LOGGER.error(e.toString(), e);
		} catch (IOException e) {
			LOGGER.error(e.toString(), e);
		}
		return null;
	}

	public void close() {
		try {
			httpclient.close();
		} catch (IOException e) {
			LOGGER.error(e.toString(), e);
		}
	}

	@Override
	public void update(LineEvent event) {

		if (event.getType().equals(LineEvent.Type.START)) {

			LOGGER.info("################## TALK!!! Start event after: " + (new Date().getTime() - startTimestamp)
					+ " ###########################");
			stopperThread = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(maxRecordLength);
					} catch (InterruptedException ex) {
						// Admitted
					}
					microphone.stop();
				}
			});
			stopperThread.start();
		}
	}
}

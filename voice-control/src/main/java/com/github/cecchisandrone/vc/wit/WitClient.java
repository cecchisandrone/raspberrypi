package com.github.cecchisandrone.vc.wit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;

import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
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

import com.github.cecchisandrone.vc.audio.Microphone;
import com.github.cecchisandrone.vc.audio.MicrophoneInputStream;

public class WitClient implements LineListener {

	// record duration, in milliseconds
	static final long RECORD_TIME = 5000;

	private String baseUri;

	private Microphone microphone;

	private CloseableHttpClient httpclient = HttpClients.createDefault();

	private Header authorizationHeader = new BasicHeader("Authorization",
			"Bearer GNOUVVQQWWBQCXHJ263FVIRSFWFIGVCE");

	private long startTimestamp;

	public WitClient(String url, Microphone microphone) throws Exception {
		URIBuilder builder = new URIBuilder(url);
		builder.addParameter("v", "20150318");
		baseUri = builder.toString();
		this.microphone = microphone;
	}

	public String sendChunkedAudio() {

		HttpPost httpPost;
		try {
			
			long time = new Date().getTime();
			
			String encoding = microphone.getAudioFormat().getEncoding() == Encoding.PCM_SIGNED ? "signed-integer"
					: "unsigned-integer";
			String bits = Integer.toString(microphone.getAudioFormat()
					.getSampleSizeInBits());
			String rate = Long.toString((long) microphone.getAudioFormat()
					.getSampleRate());
			String endian = microphone.getAudioFormat().isBigEndian() ? "big"
					: "little";
			
			microphone.open(this);
			MicrophoneInputStream inputStream = microphone.start();
			httpPost = new HttpPost(baseUri);
			InputStreamEntity reqEntity = new InputStreamEntity(inputStream, -1);
			reqEntity.setChunked(true);
			httpPost.setEntity(reqEntity);
			httpPost.addHeader(authorizationHeader);
			httpPost.addHeader("Content-Type", "audio/raw;encoding=" + encoding
					+ ";bits=" + bits + ";rate=" + rate + ";endian=" + endian
					+ ";");

			startTimestamp = new Date().getTime();
			CloseableHttpResponse response = httpclient.execute(httpPost);
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();

			StringWriter writer = new StringWriter();
			IOUtils.copy(content, writer, "UTF-8");
			String json = writer.toString();
			EntityUtils.consume(entity);
			System.out.println("Time taken: " + (new Date().getTime() - time));
			return json;

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String sendAudio(String filename) {
		HttpPost httpPost;
		try {
			long time = new Date().getTime();
			File f = new File(filename);
			httpPost = new HttpPost(baseUri);
			InputStreamEntity reqEntity = new InputStreamEntity(
					new FileInputStream(f), f.length(),
					ContentType.create("audio/wav"));

			reqEntity.setChunked(true);
			httpPost.setEntity(reqEntity);
			httpPost.addHeader(authorizationHeader);
			httpPost.addHeader("Content-Type", "audio/wav");
			System.out.println("Executing request: "
					+ httpPost.getRequestLine());

			CloseableHttpResponse response = httpclient.execute(httpPost);

			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();

			StringWriter writer = new StringWriter();
			IOUtils.copy(content, writer, "UTF-8");
			String json = writer.toString();
			EntityUtils.consume(entity);
			System.out.println("Time taken: " + (new Date().getTime() - time));
			return json;

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void close() {
		try {
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(LineEvent event) {

		if (event.getType().equals(LineEvent.Type.START)) {

			System.out
					.println("################## TALK!!! Start event after: "
							+ (new Date().getTime() - startTimestamp) + " ###########################");
			// creates a new thread that waits for a specified
			// of time before stopping
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(RECORD_TIME);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					microphone.stop();
				}
			}).start();
		}
	}
}

package com.github.cecchisandrone.vc.wit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Date;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

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

public class WitClient {

	private String baseUri;

	private CloseableHttpClient httpclient = HttpClients.createDefault();

	private AudioFormat audioFormat;

	private Header authorizationHeader = new BasicHeader("Authorization", "Bearer GNOUVVQQWWBQCXHJ263FVIRSFWFIGVCE");

	public WitClient(String url, AudioFormat audioFormat) throws URISyntaxException {
		URIBuilder builder = new URIBuilder(url);
		builder.addParameter("v", "20150318");
		baseUri = builder.toString();
		this.audioFormat = audioFormat;
	}

	public void sendChunkedAudio(InputStream inputStream) {

		long time = new Date().getTime();
		HttpPost httpPost;
		try {
			String encoding = audioFormat.getEncoding() == Encoding.PCM_SIGNED ? "signed-integer" : "unsigned-integer";
			String bits = Integer.toString(audioFormat.getSampleSizeInBits());
			String rate = Long.toString((long) audioFormat.getSampleRate());
			String endian = audioFormat.isBigEndian() ? "big" : "little";

			httpPost = new HttpPost(baseUri);

			InputStreamEntity reqEntity = new InputStreamEntity(inputStream, -1);
			reqEntity.setChunked(true);
			httpPost.setEntity(reqEntity);
			httpPost.addHeader(authorizationHeader);
			httpPost.addHeader("Content-Type", "audio/raw; encoding=" + encoding + "; bits=" + bits + "; rate=" + rate
					+ "; endian=" + endian + ";");
			System.out.println("Executing request: " + httpPost.getRequestLine());

			CloseableHttpResponse response = httpclient.execute(httpPost);
			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();

			StringWriter writer = new StringWriter();
			IOUtils.copy(content, writer, "UTF-8");
			String json = writer.toString();

			System.out.println(json);

			EntityUtils.consume(entity);
			System.out.println("Time taken: " + (new Date().getTime() - time));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendAudio(String filename) {
		HttpPost httpPost;
		try {
			long time = new Date().getTime();
			File f = new File(filename);
			httpPost = new HttpPost(baseUri);
			InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(f), f.length(),
					ContentType.create("audio/wav"));

			reqEntity.setChunked(true);
			httpPost.setEntity(reqEntity);
			httpPost.addHeader(authorizationHeader);
			httpPost.addHeader("Content-Type", "audio/wav");
			System.out.println("Executing request: " + httpPost.getRequestLine());

			CloseableHttpResponse response = httpclient.execute(httpPost);
			System.out.println("Time taken: " + (new Date().getTime() - time));
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();

			StringWriter writer = new StringWriter();
			IOUtils.copy(content, writer, "UTF-8");
			String json = writer.toString();

			System.out.println(json);

			EntityUtils.consume(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

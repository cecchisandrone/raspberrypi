package com.github.cecchisandrone.smarthome.service.camera;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Component;

import com.github.cecchisandrone.smarthome.domain.CameraConfiguration;


@Component(value = "cameraService")
public class CameraServiceImpl implements CameraService {

	private static final String TOGGLE_ALARM_PATTERN = "%s/set_alarm.cgi?mail=%d&user=%s&pwd=%s";

	private static final String VIDEO_STREAM_PATTERN = "%s/videostream.cgi?user=%s&pwd=%s";

	@Override
	public void toggleAlarm(CameraConfiguration cameraConfiguration) throws CameraServiceException {

		String urlString = String.format(TOGGLE_ALARM_PATTERN, cameraConfiguration.getHost(), cameraConfiguration.isAlarmEnabled() ? 1 : 0,
			cameraConfiguration.getUsername(), cameraConfiguration.getPassword());

		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			int responseCode = connection.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				throw new CameraServiceException("Unable to toggle alarm for camera " + cameraConfiguration.getName() + ". Response code: "
					+ responseCode);
			}

		} catch (MalformedURLException e) {
			throw new CameraServiceException(e.toString());
		} catch (IOException e) {
			throw new CameraServiceException(e.toString());
		}
	}

	@Override
	public String getVideoStreamUrl(CameraConfiguration cameraConfiguration) throws CameraServiceException {
		return String.format(VIDEO_STREAM_PATTERN, cameraConfiguration.getHost(), cameraConfiguration.getUsername(),
			cameraConfiguration.getPassword());
	}
}

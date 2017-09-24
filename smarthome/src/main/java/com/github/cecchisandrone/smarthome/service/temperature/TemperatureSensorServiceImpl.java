package com.github.cecchisandrone.smarthome.service.temperature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.github.cecchisandrone.smarthome.domain.TemperatureSensorConfiguration;
import com.github.cecchisandrone.smarthome.service.configuration.ConfigurationService;

@Component
public class TemperatureSensorServiceImpl implements TemperatureSensorService {

	private static final String TEMPERATURE_ENDPOINT = "/temp";

	private RestTemplate restTemplate;

	public TemperatureSensorServiceImpl() {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(5000);
		httpRequestFactory.setConnectTimeout(5000);
		httpRequestFactory.setReadTimeout(5000);
		restTemplate = new RestTemplate(httpRequestFactory);
	}

	@Autowired
	private ConfigurationService configurationService;

	@Override
	public double getTemperature() {

		TemperatureSensorConfiguration configuration = configurationService.getConfiguration()
				.getTemperatureSensorConfiguration();
		String temperature = restTemplate
				.getForObject("http://" + configuration.getServiceHost() + TEMPERATURE_ENDPOINT, String.class);
		return new Double(temperature);
	}
}

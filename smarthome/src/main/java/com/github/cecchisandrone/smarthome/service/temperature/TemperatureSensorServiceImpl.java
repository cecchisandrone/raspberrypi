package com.github.cecchisandrone.smarthome.service.temperature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.github.cecchisandrone.smarthome.domain.TemperatureSensorConfiguration;
import com.github.cecchisandrone.smarthome.service.configuration.ConfigurationService;

@Component
public class TemperatureSensorServiceImpl implements TemperatureSensorService {

	private static final String TEMPERATURE_ENDPOINT = "/temp";

	private RestTemplate restTemplate = new RestTemplate();

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

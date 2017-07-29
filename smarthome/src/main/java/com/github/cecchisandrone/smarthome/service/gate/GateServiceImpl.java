package com.github.cecchisandrone.smarthome.service.gate;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.github.cecchisandrone.smarthome.domain.GateConfiguration;

@Component
public class GateServiceImpl implements GateService {

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void open(GateConfiguration gateConfiguration) throws GateServiceException {

		try {
			restTemplate.postForEntity(
					"http://" + gateConfiguration.getServiceHost() + "/toggle-relay?duration={duration}", null,
					Void.class, gateConfiguration.getDuration());
		} catch (Exception e) {
			throw new GateServiceException("Unable to open gate. Reason: " + e.toString(), e);
		}
	}

}

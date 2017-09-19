package com.github.cecchisandrone.smarthome.service.temperature;

import org.springframework.stereotype.Service;

@Service
public interface TemperatureSensorService {

	public double getTemperature();
}

package com.github.cecchisandrone.smarthome.service.configuration;

import org.springframework.stereotype.Service;

import com.github.cecchisandrone.smarthome.domain.Configuration;

@Service
public interface ConfigurationService {

	public Configuration getConfiguration() throws ConfigurationServiceException;

	public void saveConfiguration(Configuration configuration) throws ConfigurationServiceException;
}

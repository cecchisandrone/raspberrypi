package it.cecchi.smarthome.service.configuration;

import it.cecchi.smarthome.domain.Configuration;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public interface ConfigurationService {

	public Configuration getConfiguration() throws ConfigurationServiceException;

	public void saveConfiguration(Configuration configuration) throws ConfigurationServiceException;
}

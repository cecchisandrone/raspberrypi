package it.cecchi.smarthome.service.configuration;

import it.cecchi.smarthome.domain.Configuration;
import it.cecchi.smarthome.persistence.repository.ConfigurationRepository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationService.class);

	@Autowired
	private ConfigurationRepository configurationRepository;

	@Override
	public Configuration getConfiguration() {

		List<Configuration> configurations = configurationRepository.findAll();
		return DataAccessUtils.singleResult(configurations);
	}

	@Override
	public void saveConfiguration(Configuration configuration) {
		configurationRepository.save(configuration);
	}
}

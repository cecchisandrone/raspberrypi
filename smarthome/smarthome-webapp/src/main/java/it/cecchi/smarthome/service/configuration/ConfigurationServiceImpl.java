package it.cecchi.smarthome.service.configuration;

import it.cecchi.smarthome.domain.Configuration;
import it.cecchi.smarthome.persistence.repository.ConfigurationRepository;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {

	@Autowired
	private ConfigurationRepository configurationRepository;

	@Override
	public Configuration getConfiguration() {

		List<Configuration> configurations = configurationRepository.findAll();
		return DataAccessUtils.singleResult(configurations);
	}

	@Override
	public void saveConfiguration(Configuration configuration) throws ConfigurationServiceException {
		try {
			configurationRepository.save(configuration);
		} catch (ConstraintViolationException e) {
			throw new ConfigurationServiceException(e.toString());
		}
	}
}

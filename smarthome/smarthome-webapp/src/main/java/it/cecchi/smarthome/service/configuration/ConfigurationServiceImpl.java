package it.cecchi.smarthome.service.configuration;

import it.cecchi.smarthome.domain.CameraConfiguration;
import it.cecchi.smarthome.domain.Configuration;
import it.cecchi.smarthome.persistence.repository.ConfigurationRepository;
import it.cecchi.smarthome.service.camera.CameraService;
import it.cecchi.smarthome.service.camera.CameraServiceException;

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

	@Autowired
	private CameraService cameraService;

	@Override
	public Configuration getConfiguration() {

		List<Configuration> configurations = configurationRepository.findAll();
		return DataAccessUtils.singleResult(configurations);
	}

	@Override
	public void saveConfiguration(Configuration configuration) throws ConfigurationServiceException {
		try {
			configurationRepository.save(configuration);

			// Toggle alarm on every camera
			for (CameraConfiguration cameraConfiguration : configuration.getCameraConfigurations()) {
				cameraService.toggleAlarm(cameraConfiguration);
			}

		} catch (ConstraintViolationException e) {
			throw new ConfigurationServiceException(e.toString());
		} catch (CameraServiceException e) {
			throw new ConfigurationServiceException(e.toString());
		}
	}
}

package com.github.cecchisandrone.smarthome.service.configuration;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.cecchisandrone.smarthome.domain.CameraConfiguration;
import com.github.cecchisandrone.smarthome.domain.Configuration;
import com.github.cecchisandrone.smarthome.persistence.repository.ConfigurationRepository;
import com.github.cecchisandrone.smarthome.service.camera.CameraService;
import com.github.cecchisandrone.smarthome.service.camera.CameraServiceException;

@Component
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {

	@Autowired
	private ConfigurationRepository configurationRepository;

	@Autowired
	private CameraService cameraService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Configuration getConfiguration() {

		List<Configuration> configurations = configurationRepository.findAll();
		return DataAccessUtils.singleResult(configurations);
	}

	@Override
	public void saveConfiguration(Configuration configuration) throws ConfigurationServiceException {
		try {

			// Check if password should be changed
			if (StringUtils.isNotEmpty(configuration.getProfile().getNewPassword())
					&& StringUtils.isNotEmpty(configuration.getProfile().getNewPassword())) {

				if (passwordEncoder.isPasswordValid(configuration.getProfile().getPassword(),
						configuration.getProfile().getOldPassword(), null)) {
					configuration.getProfile().setPassword(
							passwordEncoder.encodePassword(configuration.getProfile().getNewPassword(), null));
				} else {
					throw new ConfigurationServiceException("Old password doesn't match");
				}
			}

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

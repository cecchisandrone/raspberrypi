package com.github.cecchisandrone.smarthome.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.github.cecchisandrone.smarthome.domain.CameraConfiguration;
import com.github.cecchisandrone.smarthome.domain.Configuration;
import com.github.cecchisandrone.smarthome.service.configuration.ConfigurationService;
import com.github.cecchisandrone.smarthome.service.configuration.ConfigurationServiceException;
import com.github.cecchisandrone.smarthome.service.notification.NotificationService;

@Controller
@SessionAttributes(value = {"configuration"})
public class ConfigurationController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private ConfigurationService configurationService;

	@RequestMapping(params = "updateConfiguration", value = "/updateConfiguration", method = RequestMethod.POST)
	public ModelAndView updateConfiguration(@Valid @ModelAttribute("configuration") Configuration configuration, BindingResult result, Model model) {

		if (!result.hasErrors()) {
			try {
				configurationService.saveConfiguration(configuration);
				model.addAttribute("infoMessage", "Configuration updated");
			} catch (ConfigurationServiceException e) {
				model.addAttribute("errorMessage", "Error while saving configuration. Reason: " + e.toString());
			}
		}

		return showConfiguration();
	}

	@RequestMapping("/configuration")
	public ModelAndView showConfiguration() {

		Configuration configuration;
		try {
			configuration = configurationService.getConfiguration();
			return new ModelAndView(ViewNames.CONFIGURATION, "configuration", configuration);
		} catch (ConfigurationServiceException e) {
			return new ModelAndView(ViewNames.CONFIGURATION, "errorMessage", "Error while loading configuration. Reason: " + e.toString());
		}
	}

	@RequestMapping(params = "mailTest", value = "/updateConfiguration", method = RequestMethod.POST)
	public ModelAndView mailTest(@Valid @ModelAttribute("configuration") Configuration configuration, BindingResult result, Model model) {

		if (!result.hasErrors()) {
			try {
				notificationService.sendMail(configuration.getEmail(), "If you receive this mail, the mail configuration is correct");
			} catch (Exception e) {
				model.addAttribute("errorMessage", "Cannot send mail. Reason: " + e.toString());
			}
		}

		return new ModelAndView(ViewNames.CONFIGURATION, ViewNames.CONFIGURATION, configuration);
	}

	@RequestMapping(value = "/configuration/addCamera", method = RequestMethod.GET)
	public ModelAndView prepareAddCameraConfiguration() {
		return new ModelAndView(ViewNames.CAMERA_DETAIL, "cameraConfiguration", new CameraConfiguration());
	}

	@RequestMapping(value = "/configuration/addCamera", method = RequestMethod.POST)
	public ModelAndView addCameraConfiguration(@Valid @ModelAttribute("cameraConfiguration") CameraConfiguration cameraConfiguration, BindingResult result, @ModelAttribute("configuration") Configuration configuration) {

		if (!result.hasErrors()) {
			configuration.getCameraConfigurations().add(cameraConfiguration);
			return new ModelAndView(ViewNames.CONFIGURATION, "configuration", configuration);
		} else {
			return new ModelAndView(ViewNames.CAMERA_DETAIL);
		}
	}

	@RequestMapping(value = "/configuration/addCamera/{cameraConfigurationIndex}", method = RequestMethod.POST)
	public ModelAndView updateCameraConfiguration(@PathVariable int cameraConfigurationIndex, @Valid @ModelAttribute("cameraConfiguration") CameraConfiguration cameraConfiguration, BindingResult result, @ModelAttribute("configuration") Configuration configuration, Model model) {

		if (!result.hasErrors()) {
			configuration.getCameraConfigurations().add(cameraConfiguration);
			return new ModelAndView(ViewNames.CONFIGURATION, "configuration", configuration);
		} else {
			return new ModelAndView(ViewNames.CAMERA_DETAIL);
		}
	}

	@RequestMapping(value = "/configuration/removeCamera/{cameraConfigurationIndex}", method = RequestMethod.GET)
	public ModelAndView removeCameraConfiguration(@PathVariable int cameraConfigurationIndex, @ModelAttribute("configuration") Configuration configuration, Model model) {

		configuration.getCameraConfigurations().remove(cameraConfigurationIndex);
		return new ModelAndView(ViewNames.CONFIGURATION, "configuration", configuration);
	}

	@RequestMapping(value = "/configuration/editCamera/{cameraConfigurationIndex}", method = RequestMethod.GET)
	public ModelAndView editCameraConfiguration(@PathVariable int cameraConfigurationIndex, @ModelAttribute("configuration") Configuration configuration, Model model) {

		ModelAndView modelAndView = new ModelAndView(ViewNames.CAMERA_DETAIL, "cameraConfiguration", configuration.getCameraConfigurations().get(
			cameraConfigurationIndex));
		model.addAttribute("cameraConfigurationIndex", cameraConfigurationIndex);
		return modelAndView;
	}

	@RequestMapping(value = "/configuration/editCamera/{cameraConfigurationIndex}", method = RequestMethod.POST)
	public ModelAndView editCameraConfiguration(@PathVariable int cameraConfigurationIndex, @Valid @ModelAttribute("cameraConfiguration") CameraConfiguration cameraConfiguration, @ModelAttribute("configuration") Configuration configuration, Model model) {

		configuration.getCameraConfigurations().remove(cameraConfigurationIndex);
		configuration.getCameraConfigurations().add(cameraConfigurationIndex, cameraConfiguration);
		return new ModelAndView(ViewNames.CONFIGURATION, "configuration", configuration);
	}

	@RequestMapping(value = "/configuration/toggleEnabledAllCameras/{status}", method = RequestMethod.GET)
	public ModelAndView toggleEnabledAllCameras(@PathVariable boolean status, @ModelAttribute("configuration") Configuration configuration, Model model) {

		for (CameraConfiguration cameraConfiguration : configuration.getCameraConfigurations()) {
			cameraConfiguration.setEnabled(status);
		}
		return new ModelAndView(ViewNames.CONFIGURATION, "configuration", configuration);
	}

	@RequestMapping(value = "/configuration/toggleAlarmAllCameras/{status}", method = RequestMethod.GET)
	public ModelAndView toggleAlarmAllCameras(@PathVariable boolean status, @ModelAttribute("configuration") Configuration configuration, Model model) {

		for (CameraConfiguration cameraConfiguration : configuration.getCameraConfigurations()) {
			cameraConfiguration.setAlarmEnabled(status);
		}
		return new ModelAndView(ViewNames.CONFIGURATION, "configuration", configuration);
	}
}

package com.github.cecchisandrone.smarthome.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.github.cecchisandrone.smarthome.domain.Configuration;
import com.github.cecchisandrone.smarthome.domain.Profile;
import com.github.cecchisandrone.smarthome.service.configuration.ConfigurationService;
import com.github.cecchisandrone.smarthome.service.configuration.ConfigurationServiceException;

@Controller
@SessionAttributes(value = { "profile" })
public class ProfileController {

	@Autowired
	private ConfigurationService configurationService;

	private Configuration configuration;

	@RequestMapping("/profile")
	public ModelAndView profile() {

		try {
			configuration = configurationService.getConfiguration();
			return new ModelAndView(ViewNames.PROFILE, "profile", configuration.getProfile());
		} catch (ConfigurationServiceException e) {
			return new ModelAndView(ViewNames.PROFILE, "errorMessage",
					"Error while loading configuration. Reason: " + e.toString());
		}
	}

	@RequestMapping("/updateProfile")
	public ModelAndView updateProfile(@ModelAttribute("profile") Profile profile, BindingResult result, Model model) {

		if (!result.hasErrors()) {
			if (StringUtils.isEmpty(profile.getNewPassword()) && StringUtils.isNotEmpty(profile.getOldPassword())
					|| StringUtils.isNotEmpty(profile.getNewPassword())
							&& StringUtils.isEmpty(profile.getOldPassword())) {
				model.addAttribute("errorMessage", "Both old password and new password should be specified");
			} else {
				configuration.setProfile(profile);
				try {
					configurationService.saveConfiguration(configuration);
					model.addAttribute("infoMessage", "Profile updated");
				} catch (ConfigurationServiceException e) {
					return new ModelAndView(ViewNames.PROFILE, "errorMessage",
							"Error while saving profile. Reason: " + e.toString());
				}
			}
		}
		return new ModelAndView(ViewNames.PROFILE, "profile", profile);
	}

}

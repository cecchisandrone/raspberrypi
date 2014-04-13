package it.cecchi.smarthome.web;

import it.cecchi.smarthome.domain.Configuration;
import it.cecchi.smarthome.service.configuration.ConfigurationService;
import it.cecchi.smarthome.service.configuration.ConfigurationServiceException;
import it.cecchi.smarthome.service.notification.NotificationService;
import it.cecchi.smarthome.service.raspsonar.RaspsonarService;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes(value = { "configuration" })
public class ConfigurationController {

	@Autowired
	private RaspsonarService sonarService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private ConfigurationService configurationService;

	@RequestMapping(params = "updateConfiguration", value = "/updateConfiguration", method = RequestMethod.POST)
	public String updateConfiguration(@Valid @ModelAttribute("configuration") Configuration configuration,
			BindingResult result, Model model) {

		if (!result.hasErrors()) {
			try {
				configurationService.saveConfiguration(configuration);
				model.addAttribute("infoMessage", "Configuration updated");
			} catch (ConfigurationServiceException e) {
				model.addAttribute("errorMessage", "Error while saving configuration. Reason: " + e.toString());
			}
		}

		return ViewNames.CONFIGURATION;
	}

	@RequestMapping("/configuration")
	public ModelAndView showConfiguration() {

		Configuration configuration;
		try {
			configuration = configurationService.getConfiguration();
			return new ModelAndView(ViewNames.CONFIGURATION, "configuration", configuration);
		} catch (ConfigurationServiceException e) {
			return new ModelAndView(ViewNames.CONFIGURATION, "errorMessage",
					"Error while loading configuration. Reason: " + e.toString());
		}
	}

	@RequestMapping(params = "mailTest", value = "/updateConfiguration", method = RequestMethod.POST)
	public ModelAndView mailTest(@Valid @ModelAttribute("configuration") Configuration configuration,
			BindingResult result, Model model) {

		if (!result.hasErrors()) {
			try {
				notificationService.sendMail(configuration.getEmail(),
						"If you receive this mail, the mail configuration is correct");
			} catch (Exception e) {
				model.addAttribute("errorMessage", "Cannot send mail. Reason: " + e.toString());
			}
		}

		return new ModelAndView(ViewNames.CONFIGURATION, ViewNames.CONFIGURATION, configuration);
	}
}

package it.cecchi.smarthome.web;

import it.cecchi.smarthome.domain.Configuration;
import it.cecchi.smarthome.service.SonarService;
import it.cecchi.smarthome.service.SonarService.PropertyName;
import it.cecchi.smarthome.service.SonarServiceException;

import java.util.Properties;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes
public class ConfigurationController {

	@Autowired
	private SonarService sonarService;

	@Autowired
	private MailSender mailSender;

	@RequestMapping(value = "/editConfiguration", method = RequestMethod.POST)
	public String updateConfiguration(@Valid @ModelAttribute("configuration") Configuration configuration, BindingResult result, Model model) {

		if (!result.hasErrors()) {
			Properties p = sonarService.getProperties();
			p.put(PropertyName.EMAIL.getPropertyName(), configuration.getEmail());
			try {
				sonarService.saveProperties(p);
			} catch (SonarServiceException e) {
				model.addAttribute("errorMessage", "Error while saving configuration. Reason: " + e.toString());
			}
			model.addAttribute("infoMessage", "Configuration updated");
		}

		return "configuration";
	}

	@RequestMapping("/editConfiguration")
	public ModelAndView showConfiguration() {

		Properties p = sonarService.getProperties();
		Configuration configuration = new Configuration();
		configuration.setEmail(p.getProperty(PropertyName.EMAIL.getPropertyName()));
		return new ModelAndView("configuration", "configuration", configuration);
	}

	@RequestMapping(value = "/configuration/mailTest", method = RequestMethod.GET)
	public ModelAndView testMail(@Valid @ModelAttribute("configuration") Configuration configuration, Model model) {

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("raspsonar");
			message.setSubject("raspsonar test mail");
			message.setText("If you receive this mail, the mail configuration of raspsonar is correct");
			message.setTo(configuration.getEmail());
			mailSender.send(message);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Cannot send mail. Reason: " + e.toString());
		}

		return new ModelAndView("configuration", "configuration", configuration);
	}
}

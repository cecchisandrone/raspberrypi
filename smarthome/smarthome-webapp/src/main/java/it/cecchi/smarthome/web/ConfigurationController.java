package it.cecchi.smarthome.web;

import it.cecchi.smarthome.domain.Configuration;
import it.cecchi.smarthome.domain.Task;
import it.cecchi.smarthome.service.RaspsonarService;
import it.cecchi.smarthome.service.RaspsonarService.PropertyName;
import it.cecchi.smarthome.service.RaspsonarServiceException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	private @Value("${application.name}")
	String applicationName;

	@Autowired
	private RaspsonarService sonarService;

	@Autowired
	private MailSender mailSender;

	@RequestMapping(params = "updateConfiguration", value = "/updateConfiguration", method = RequestMethod.POST)
	public String updateConfiguration(@Valid @ModelAttribute("configuration") Configuration configuration, BindingResult result, Model model) {

		if (!result.hasErrors()) {
			Properties p = sonarService.getProperties();
			p.put(PropertyName.EMAIL.getPropertyName(), configuration.getEmail());
			p.put(PropertyName.SERVICE_URL.getPropertyName(), configuration.getServiceUrl());
			try {
				sonarService.saveProperties(p);
			} catch (RaspsonarServiceException e) {
				model.addAttribute("errorMessage", "Error while saving configuration. Reason: " + e.toString());
			}
			model.addAttribute("infoMessage", "Configuration updated");
		}

		return ViewNames.CONFIGURATION;
	}

	@RequestMapping("/configuration")
	public ModelAndView showConfiguration() {

		Properties p = sonarService.getProperties();
		Configuration configuration = new Configuration();
		configuration.setEmail(p.getProperty(PropertyName.EMAIL.getPropertyName()));
		configuration.setServiceUrl(p.getProperty(PropertyName.SERVICE_URL.getPropertyName()));

		ModelAndView mav = new ModelAndView(ViewNames.CONFIGURATION, "configuration", configuration);

		mav.addObject("configurationFolder", sonarService.getConfigurationFile());

		return mav;
	}

	@RequestMapping(params = "mailTest", value = "/updateConfiguration", method = RequestMethod.POST)
	public ModelAndView mailTest(@Valid @ModelAttribute("configuration") Configuration configuration, BindingResult result, Model model) {

		if (!result.hasErrors()) {
			try {
				SimpleMailMessage message = new SimpleMailMessage();
				message.setFrom(applicationName);
				message.setSubject(applicationName + " test mail");
				message.setText("If you receive this mail, the mail configuration of " + applicationName + " is correct");
				message.setTo(configuration.getEmail());
				mailSender.send(message);
			} catch (Exception e) {
				model.addAttribute("errorMessage", "Cannot send mail. Reason: " + e.toString());
			}
		}

		return new ModelAndView(ViewNames.CONFIGURATION, ViewNames.CONFIGURATION, configuration);
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ModelAndView test() {

		Task task1 = new Task(1, "title1", "text1", new Date());
		Task task2 = new Task(1, "title2", "text2", new Date());
		Task task3 = new Task(1, "title3", "text3", new Date());

		List<Task> tasks = new ArrayList<Task>();
		tasks.add(task1);
		tasks.add(task2);
		tasks.add(task3);

		return new ModelAndView("testLayout", "tasks", tasks);
	}
}

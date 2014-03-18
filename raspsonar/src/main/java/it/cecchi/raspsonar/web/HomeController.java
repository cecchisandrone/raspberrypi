package it.cecchi.raspsonar.web;

import it.cecchi.raspsonar.service.SonarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	@Autowired
	private SonarService sonarService;

	@RequestMapping(value = "/")
	public ModelAndView home() {

		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("waterLevel", sonarService.getWaterLevel());
		modelAndView.addObject("configurationFolder", sonarService.getConfigurationFile());

		return modelAndView;
	}
}

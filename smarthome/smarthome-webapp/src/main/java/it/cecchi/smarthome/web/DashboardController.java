package it.cecchi.smarthome.web;

import it.cecchi.smarthome.service.SonarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class DashboardController {

	@Autowired
	private SonarService sonarService;

	@RequestMapping(value = {"/dashboard", "/"}, method = RequestMethod.GET)
	public ModelAndView home() {

		ModelAndView modelAndView = new ModelAndView(ViewNames.DASHBOARD);
		modelAndView.addObject("waterLevel", sonarService.getWaterLevel());
		modelAndView.addObject("configurationFolder", sonarService.getConfigurationFile());

		return modelAndView;
	}
}

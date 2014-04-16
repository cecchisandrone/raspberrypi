package it.cecchi.smarthome.web;

import it.cecchi.smarthome.domain.Configuration;
import it.cecchi.smarthome.service.configuration.ConfigurationService;
import it.cecchi.smarthome.service.configuration.ConfigurationServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class CameraController {

	@Autowired
	private ConfigurationService configurationService;

	@RequestMapping(value = "/cameras", method = RequestMethod.GET)
	public ModelAndView loadCameras() {

		Configuration configuration = null;
		try {
			configuration = configurationService.getConfiguration();
		} catch (ConfigurationServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView(ViewNames.CAMERAS, "configuration", configuration);
	}
}

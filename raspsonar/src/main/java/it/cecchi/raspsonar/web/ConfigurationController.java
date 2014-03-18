package it.cecchi.raspsonar.web;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes
public class ConfigurationController {

	@RequestMapping(value = "/saveConfiguration", method = RequestMethod.POST)
	public String addContact(@ModelAttribute("configuration") Configuration configuration, BindingResult result) {

		System.out.println("Email:" + configuration.getEmail());

		return "redirect:configuration";
	}

	@RequestMapping("/editConfiguration")
	public ModelAndView showConfiguration() {

		return new ModelAndView("configuration", "configuration", new Configuration());
	}
}

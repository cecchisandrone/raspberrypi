package it.cecchi.smarthome.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

	// Login form
	@RequestMapping("/login")
	public String login() {
		return ViewNames.LOGIN;
	}

	// Login form with error
	@RequestMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return ViewNames.LOGIN;
	}
}

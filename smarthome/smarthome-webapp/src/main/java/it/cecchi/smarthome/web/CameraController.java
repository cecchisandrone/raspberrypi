package it.cecchi.smarthome.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class CameraController {

	@RequestMapping(value = "/cameras", method = RequestMethod.GET)
	public String loadCameras() {

		return ViewNames.CAMERAS;
	}
}

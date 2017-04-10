package com.github.cecchisandrone.smarthome.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.github.cecchisandrone.smarthome.domain.Configuration;
import com.github.cecchisandrone.smarthome.service.configuration.ConfigurationService;
import com.github.cecchisandrone.smarthome.service.configuration.ConfigurationServiceException;
import com.github.cecchisandrone.smarthome.service.raspsonar.RaspsonarService;
import com.github.cecchisandrone.smarthome.service.raspsonar.RaspsonarServiceException;
import com.github.cecchisandrone.smarthome.service.zm.ZoneMinderService;
import com.github.cecchisandrone.smarthome.service.zm.ZoneMinderServiceException;
import com.github.cecchisandrone.smarthome.utils.ZoneMinderUtils;

/**
 * Handles requests for the application home page.
 */
@Controller
public class DashboardController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private RaspsonarService raspsonarService;

	@Autowired
	private ZoneMinderService zoneminderService;

	@Autowired
	private ConfigurationService configurationService;

	@RequestMapping(value = { "/dashboard", "/" }, method = RequestMethod.GET)
	public ModelAndView home() {

		Configuration configuration = null;
		ModelAndView modelAndView = new ModelAndView(ViewNames.DASHBOARD);
		try {
			configuration = configurationService.getConfiguration();
		} catch (ConfigurationServiceException e) {
			modelAndView.addObject("errorMessage", e.toString());
			LOGGER.error(e.getMessage(), e);
			return modelAndView;
		}

		try {
			modelAndView.addObject("waterLevel", raspsonarService.getDistance(false));
			modelAndView.addObject("relayStatus", raspsonarService.isRelayStatus());
			modelAndView.addObject("distanceChartUrl", raspsonarService.getDistanceChartUrl());
		} catch (RaspsonarServiceException e) {
			modelAndView.addObject("errorMessage", e.toString());
		}

		try {
			modelAndView.addObject("zmStatus",
					ZoneMinderUtils.pingHost(configuration.getZoneMinderConfiguration().getZmHost()));
		} catch (IOException e) {
			// Deliberately do nothing here
		}
		return modelAndView;
	}

	@RequestMapping(value = { "/dashboard/resetAverageDistance" }, method = RequestMethod.GET)
	public ModelAndView resetAverageDistance() {

		ModelAndView modelAndView = new ModelAndView(ViewNames.DASHBOARD);
		try {
			modelAndView.addObject("waterLevel", raspsonarService.getDistance(true));
		} catch (RaspsonarServiceException e) {
			modelAndView.addObject("errorMessage", e.toString());
			LOGGER.error(e.getMessage(), e);
		}
		return modelAndView;
	}

	@RequestMapping(value = { "/dashboard/waterPump/{status}" }, method = RequestMethod.GET)
	public ModelAndView toggleRelay(@PathVariable boolean status, Model model) {

		try {
			raspsonarService.toggleRelay(status);
		} catch (RaspsonarServiceException e) {
			model.addAttribute("errorMessage", e.toString());
		}
		return home();
	}

	@RequestMapping(value = { "/dashboard/zoneminder/shutdown" }, method = RequestMethod.POST)
	public Model shutdownZoneminderHost(Model model) {

		try {
			Configuration configuration = configurationService.getConfiguration();
			configurationService.saveConfiguration(configuration);
			zoneminderService.shutdownZmHost(configuration.getZoneMinderConfiguration());
			model.addAttribute("status", "Operation completed successfully");
		} catch (ZoneMinderServiceException | ConfigurationServiceException e) {
			model.addAttribute("status", e.toString());
			LOGGER.error(e.getMessage(), e);
		}
		return model;
	}

	@RequestMapping(value = { "/dashboard/zoneminder/wake-up" }, method = RequestMethod.POST)
	public Model wakeUpZoneminderHost(Model model) {

		try {
			Configuration configuration = configurationService.getConfiguration();
			configurationService.saveConfiguration(configuration);
			zoneminderService.wakeUpZmHost(configuration.getZoneMinderConfiguration());
			model.addAttribute("status", "Operation completed successfully");
		} catch (ZoneMinderServiceException | ConfigurationServiceException e) {
			model.addAttribute("status", e.toString());
			LOGGER.error(e.getMessage(), e);
		}
		return model;
	}

	@RequestMapping("/error")
	public String error(HttpServletRequest request, Model model) {

		model.addAttribute("errorCode", request.getAttribute("javax.servlet.error.status_code"));
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		String errorMessage = null;
		if (throwable != null) {
			errorMessage = throwable.toString();
		}
		model.addAttribute("errorMessage", errorMessage);
		return ViewNames.ERROR;
	}
}

package com.github.cecchisandrone.arpa.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.cecchisandrone.arpa.module.NavigationModule;
import com.github.cecchisandrone.raspio.gpio.MotorDevice;
import com.github.cecchisandrone.raspio.gpio.MotorDevice.Motor;
import com.github.cecchisandrone.raspio.gpio.SonarDevice;
import com.github.cecchisandrone.raspio.service.DeviceManager;
import com.github.cecchisandrone.raspio.service.IOServiceException;

public class RobotNavigator {

	private int currentSpeedOnX = 0;

	private int currentSpeedOnY = 0;

	private static final int ROTATION_SPEED = 70;

	private static final Logger LOGGER = LoggerFactory.getLogger(NavigationModule.class);

	@Autowired
	private DeviceManager deviceManager;

	private String motorDeviceId;

	private MotorDevice motorDevice;

	private String sonarDeviceId;

	private SonarDevice sonarDevice;

	public void setMotorDeviceId(String motorDeviceId) {
		this.motorDeviceId = motorDeviceId;
	}

	public void setSonarDeviceId(String sonarDeviceId) {
		this.sonarDeviceId = sonarDeviceId;
	}

	public int getCurrentSpeedOnX() {
		return currentSpeedOnX;
	}

	public int getCurrentSpeedOnY() {
		return currentSpeedOnY;
	}

	public void init() {
		// Devices initialization
		try {
			motorDevice = (MotorDevice) deviceManager.getDevice(MotorDevice.class, motorDeviceId);
			sonarDevice = (SonarDevice) deviceManager.getDevice(SonarDevice.class, sonarDeviceId);
		} catch (IOServiceException e) {
			LOGGER.error(e.toString(), e);
		}
	}

	public synchronized void moveStraight(int speed) {
		currentSpeedOnY = Math.abs(speed);
		motorDevice.changeSpeed(Motor.LEFT, -speed);
		motorDevice.changeSpeed(Motor.RIGHT, -speed);
	}

	public synchronized void rotate(int speed) {
		currentSpeedOnX = Math.abs(speed);
		motorDevice.changeSpeed(Motor.LEFT, -speed);
		motorDevice.changeSpeed(Motor.RIGHT, speed);
	}

	public synchronized void stop() {

		currentSpeedOnX = currentSpeedOnY = 0;
		motorDevice.changeSpeed(Motor.LEFT, 0);
		motorDevice.changeSpeed(Motor.RIGHT, 0);
	}

	public synchronized void moveStraight(int speed, long time) {
		moveStraight(speed);
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			LOGGER.error(e.toString(), e);
		}
		stop();
	}

	public synchronized void rotate(int speed, long time) {
		rotate(speed);
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			LOGGER.error(e.toString(), e);
		}
		stop();
	}

	public synchronized void rotate(double angle) {

		long time = (long) (-13.52 * (angle * angle) + 382 * Math.abs(angle) + 233.333);

		rotate((int) Math.signum(angle) * ROTATION_SPEED, time);
	}

	public double getRange() {
		return sonarDevice.getRange();
	}
}

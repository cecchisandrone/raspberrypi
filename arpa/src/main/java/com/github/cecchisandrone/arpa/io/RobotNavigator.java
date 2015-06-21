package com.github.cecchisandrone.arpa.io;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.cecchisandrone.arpa.module.NavigationModule;
import com.github.cecchisandrone.arpa.util.Utils;
import com.github.cecchisandrone.raspio.gpio.MotorDevice;
import com.github.cecchisandrone.raspio.gpio.MotorDevice.Motor;
import com.github.cecchisandrone.raspio.gpio.SonarDevice;
import com.github.cecchisandrone.raspio.i2c.HMC5883L;
import com.github.cecchisandrone.raspio.service.DeviceManager;
import com.github.cecchisandrone.raspio.service.IOServiceException;

public class RobotNavigator {

	private int currentSpeedOnX = 0;

	private int currentSpeedOnY = 0;

	private static final int ROTATION_SPEED = 100;

	private static final Logger LOGGER = LoggerFactory.getLogger(NavigationModule.class);

	@Autowired
	private DeviceManager deviceManager;

	private String motorDeviceId;

	private MotorDevice motorDevice;

	private String sonarDeviceId;

	private SonarDevice sonarDevice;

	private HMC5883L magnetometer;

	public void setMotorDeviceId(String motorDeviceId) {
		this.motorDeviceId = motorDeviceId;
	}

	public void setSonarDeviceId(String sonarDeviceId) {
		this.sonarDeviceId = sonarDeviceId;
	}

	public void setMagnetometer(HMC5883L magnetometer) {
		this.magnetometer = magnetometer;
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
		if (Math.abs(speed) > MotorDevice.PWM_RANGE) {
			speed = (int) (Math.signum(speed) * MotorDevice.PWM_RANGE);
		}
		currentSpeedOnY = Math.abs(speed);
		motorDevice.changeSpeed(Motor.LEFT, -speed);
		motorDevice.changeSpeed(Motor.RIGHT, -speed);
	}

	public synchronized void rotate(int speed) {
		if (Math.abs(speed) > MotorDevice.PWM_RANGE) {
			speed = (int) (Math.signum(speed) * MotorDevice.PWM_RANGE);
		}
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

		angle = Math.PI / 2;
		double angleDeg = Utils.rad2deg(angle);
		double bearing, initialBearing;
		initialBearing = bearing = getBearing();
		do {
			rotate((int) Math.signum(angle) * ROTATION_SPEED, 100);
			bearing = getBearing();
			System.out.println(bearing);

		} while (Math.abs(initialBearing - bearing) < angleDeg);
	}

	public double getRange() {
		return sonarDevice.getRange();
	}

	public Double getBearing() {
		try {
			return magnetometer.getBearing();
		} catch (IOException e) {
			LOGGER.error(e.toString(), e);
			throw new RuntimeException(e);
		}
	}
}

package com.github.cecchisandrone.raspio.io;

import com.github.cecchisandrone.raspio.io.MotorDevice.Motor;
import com.pi4j.io.gpio.RaspiPin;

public class MotorDeviceTest {

	public static void main(String[] args) throws InterruptedException {

		MotorDevice motorDevice = new MotorDevice();
		motorDevice.setEn1Pin(15);
		motorDevice.setEn2Pin(16);
		motorDevice.setIn1Pin(RaspiPin.GPIO_00);
		motorDevice.setIn2Pin(RaspiPin.GPIO_02);
		motorDevice.setIn3Pin(RaspiPin.GPIO_12);
		motorDevice.setIn4Pin(RaspiPin.GPIO_03);
		motorDevice.init();

		for (int speed = 0; speed < 100; speed = speed + 10) {
			motorDevice.changeSpeed(Motor.LEFT, speed);
			Thread.sleep(1000);
		}
		motorDevice.changeSpeed(Motor.LEFT, 0);

		for (int speed = 0; speed < 100; speed = speed + 10) {
			motorDevice.changeSpeed(Motor.RIGHT, speed);
			Thread.sleep(1000);
		}
		motorDevice.changeSpeed(Motor.RIGHT, 0);
	}
}

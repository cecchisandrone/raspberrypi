package com.github.cecchisandrone.raspio.io;

import com.pi4j.io.gpio.RaspiPin;

public class SonarDeviceTest {

	public static void main(String[] args) {

		SonarDevice sonar = new SonarDevice();
		sonar.setEcho(RaspiPin.GPIO_04);
		sonar.setTrigger(RaspiPin.GPIO_05);
		System.out.println(sonar.getRange() + " cm");
	}
}

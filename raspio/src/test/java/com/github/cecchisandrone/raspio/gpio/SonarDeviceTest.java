package com.github.cecchisandrone.raspio.gpio;

import com.github.cecchisandrone.raspio.gpio.SonarDevice;
import com.pi4j.io.gpio.RaspiPin;

public class SonarDeviceTest {

	public static void main(String[] args) throws InterruptedException {

		SonarDevice sonar = new SonarDevice();
		sonar.setTrigger(RaspiPin.GPIO_13);
		sonar.setEcho(RaspiPin.GPIO_14);
		sonar.init();
		for (;;) {
			System.out.println(sonar.getRange() + " cm");
			Thread.sleep(500);
		}
	}
}

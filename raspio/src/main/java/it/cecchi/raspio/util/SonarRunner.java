package it.cecchi.raspio.util;

import it.cecchi.raspio.io.SonarDevice;

import com.pi4j.io.gpio.RaspiPin;

public class SonarRunner {

	public static void main(String[] args) {

		while (true) {
			SonarDevice sonar = new SonarDevice(RaspiPin.GPIO_04, RaspiPin.GPIO_05);
			System.out.println(sonar.getRange() + " cm");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

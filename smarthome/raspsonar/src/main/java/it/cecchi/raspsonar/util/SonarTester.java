package it.cecchi.raspsonar.util;

import it.cecchi.raspsonar.service.SonarSensor;


public class SonarTester {

	public static void main(String[] args) {

		while (true) {
			double distance = SonarSensor.getInstance().getRange();
			System.out.println(distance + " cm");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

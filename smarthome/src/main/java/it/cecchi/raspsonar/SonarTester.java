package it.cecchi.raspsonar;

import it.cecchi.raspsonar.gpio.RangeFinder;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public class SonarTester {

	public static void main(String[] args) {

		while (true) {
			double distance = RangeFinder.getInstance().getRange();
			System.out.println(distance + " cm");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}
	}

}

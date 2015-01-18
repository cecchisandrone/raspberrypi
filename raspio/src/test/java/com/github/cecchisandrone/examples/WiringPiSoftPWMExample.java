package com.github.cecchisandrone.examples;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

public class WiringPiSoftPWMExample {

	public static void main(String[] args) throws InterruptedException {

		// initialize wiringPi library
		Gpio.wiringPiSetup();
		GpioController gpio = GpioFactory.getInstance();

		// // create soft-pwm pins (min=0 ; max=100)
		SoftPwm.softPwmCreate(16, 0, 100);

		// Sonar Sensor pins
		GpioPinDigitalOutput motor1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "M1", PinState.LOW);
		GpioPinDigitalOutput motor2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "M2", PinState.LOW);
		// GpioPinDigitalOutput enable =
		// gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, "EN",
		// PinState.HIGH);

		motor1.high();
		motor2.low();

		for (int i = 0; i <= 100; i = i + 10) {
			SoftPwm.softPwmWrite(16, i);
			Thread.sleep(1000);
		}

		for (int i = 100; i >= 0; i = i - 10) {
			SoftPwm.softPwmWrite(16, i);
			Thread.sleep(1000);
		}

		gpio.shutdown();
	}
}

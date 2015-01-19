package com.github.cecchisandrone.raspio.io;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.SoftPwm;

public class MotorDevice extends AbstractDevice {

	public enum Motor {
		LEFT, RIGHT
	}

	private Pin in1Pin;

	private Pin in2Pin;

	private Pin in3Pin;

	private Pin in4Pin;

	private Integer en1Pin;

	private Integer en2Pin;

	private GpioPinDigitalOutput in1;

	private GpioPinDigitalOutput in2;

	private GpioPinDigitalOutput in3;

	private GpioPinDigitalOutput in4;

	public void setIn1Pin(Pin in1Pin) {
		this.in1Pin = in1Pin;
	}

	public void setIn2Pin(Pin in2Pin) {
		this.in2Pin = in2Pin;
	}

	public void setIn3Pin(Pin in3Pin) {
		this.in3Pin = in3Pin;
	}

	public void setIn4Pin(Pin in4Pin) {
		this.in4Pin = in4Pin;
	}

	public void setEn1Pin(int en1Pin) {
		this.en1Pin = en1Pin;
	}

	public void setEn2Pin(int en2Pin) {
		this.en2Pin = en2Pin;
	}

	@Override
	public void internalInit() {

		if (in1Pin == null || in2Pin == null || in3Pin == null || in4Pin == null || en1Pin == null || en2Pin == null) {
			throw new IllegalArgumentException("Not all the required pin are set");
		}

		in1 = gpio.provisionDigitalOutputPin(in1Pin, "IN1", PinState.LOW);
		in2 = gpio.provisionDigitalOutputPin(in2Pin, "IN2", PinState.LOW);
		in3 = gpio.provisionDigitalOutputPin(in3Pin, "IN3", PinState.LOW);
		in4 = gpio.provisionDigitalOutputPin(in4Pin, "IN4", PinState.LOW);

		SoftPwm.softPwmCreate(en1Pin, 0, 100);
		SoftPwm.softPwmCreate(en2Pin, 0, 100);
	}

	public void changeSpeed(Motor motor, int speed) {

		checkInitialized();

		if (speed > 100 || speed < -100) {
			throw new IllegalArgumentException(speed + " is an illegal value for speed parameter");
		}

		GpioPinDigitalOutput p1, p2;
		Integer e = null;
		if (speed > 0) {
			if (motor == Motor.LEFT) {
				p1 = in1;
				p2 = in2;
				e = en1Pin;
			} else {
				p1 = in3;
				p2 = in4;
				e = en2Pin;
			}
		} else {
			if (motor == Motor.LEFT) {
				p2 = in1;
				p1 = in2;
				e = en1Pin;
			} else {
				p2 = in3;
				p1 = in4;
				e = en2Pin;
			}
		}

		SoftPwm.softPwmWrite(e, speed);
		p1.high();
		p2.low();
	}

	@Override
	public void internalInit(String configurationString) {
		// Prepare pins
		// String[] pinNumbers = confString.split(",");
		// Pin[] pins = new Pin[pinNumbers.length];
		// for (int i = 0; i < pins.length; i++) {
		// pins[i] = getPinByPinNumber(pinNumbers[i]);
		// }
	}
}

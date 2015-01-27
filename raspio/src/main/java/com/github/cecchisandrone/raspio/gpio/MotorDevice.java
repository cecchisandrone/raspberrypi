package com.github.cecchisandrone.raspio.gpio;

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

	/**
	 * Change the selected motor speed. Using negative/positive values for speed you decide the rotation direction 
	 * for the motor 
	 * 
	 * @param motor - the selected motor
	 * @param speed - the speed value. -100 <= speed <= 100
	 */
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

		SoftPwm.softPwmWrite(e, Math.abs(speed));
		p1.high();
		p2.low();
	}

	/**
	 * Expect a configuration string like this: in1,in2,in3,in4,en1,en2. Where:
	 * - in1, in2: integer representing pin to control Motor1 direction 
	 * - in3, in4: integer representing pin to control Motor2 direction 
	 * - en1: integer representing pin to enable Motor1 (PWM) - en2: integer representing pin to enable Motor2 (PWM)
	 */
	@Override
	public void loadConfiguration(String configurationString) {

		String[] pinNumbers = configurationString.split(",");
		if (pinNumbers.length != 6) {
			throw new IllegalArgumentException("Bad format of configuration string");
		}

		Pin[] pins = new Pin[4];
		for (int i = 0; i < 4; i++) {
			pins[i] = getPinByPinNumber(pinNumbers[i]);
		}

		this.setIn1Pin(pins[0]);
		this.setIn2Pin(pins[1]);
		this.setIn3Pin(pins[2]);
		this.setIn4Pin(pins[3]);
		this.setEn1Pin(Integer.parseInt(pinNumbers[4]));
		this.setEn2Pin(Integer.parseInt(pinNumbers[5]));
	}
}

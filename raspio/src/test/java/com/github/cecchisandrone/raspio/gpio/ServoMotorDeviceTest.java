package com.github.cecchisandrone.raspio.gpio;

public class ServoMotorDeviceTest {

	public static void main(String[] args) throws InterruptedException {

		ServoMotorDevice motorDevice = new ServoMotorDevice();
		motorDevice.setControlPin(16);
		motorDevice.init();

		ServoMotorDevice motorDevice2 = new ServoMotorDevice();
		motorDevice2.setControlPin(18);
		motorDevice2.init();

		int val = 100;
		int direction = 1;
		while (true) {

			motorDevice.changePosition(val);
			motorDevice2.changePosition(val);
			Thread.sleep(5);

			if (val == 100) {
				direction -= 1;
			} else if (val == 0) {
				direction = 1;
			}

			val += direction;
		}
	}
}

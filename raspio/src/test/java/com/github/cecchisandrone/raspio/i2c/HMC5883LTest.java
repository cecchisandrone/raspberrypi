package com.github.cecchisandrone.raspio.i2c;

import java.io.IOException;

public class HMC5883LTest {

	public static void main(String[] args) throws IOException, InterruptedException {
		HMC5883L device = new HMC5883L(1, 0x1E);
		device.setFlipY(true);
		device.setOffsetX(28.52);
		device.setOffsetY(-57.04);
		// device.calibrate();

		for (int i = 0; i < 200; i++) {
			System.out.println(device.getBearing());
			Thread.sleep(200);
		}

	}

}

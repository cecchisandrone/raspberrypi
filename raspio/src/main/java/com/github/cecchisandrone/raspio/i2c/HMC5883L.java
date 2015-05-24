package com.github.cecchisandrone.raspio.i2c;

import java.io.IOException;

import org.slf4j.Logger;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

/**
 * Interface to HMC5883L digital magnetometer device 
 * 
 * http://www51.honeywell.com/aero/common/documents/myaerospacecatalog-documents/Defense_Brochures-documents/HMC5883L_3-Axis_Digital_Compass_IC.pdf
 * 
 * @author Alessandro
 *
 */
public class HMC5883L {

	public class RawValues {

		private double x;

		private double y;

		private double z;

		public RawValues(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}

		public double getZ() {
			return z;
		}

		public void setZ(double z) {
			this.z = z;
		}
	}

	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(HMC5883L.class);

	private I2CDevice device;

	private double offsetX;

	private double offsetY;

	private double offsetZ;

	private double scale = 0.92; // Default scale

	private boolean flipY = false;

	public void setFlipY(boolean flipY) {
		this.flipY = flipY;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public void setOffsetX(double offsetX) {
		this.offsetX = offsetX;
	}

	public void setOffsetY(double offsetY) {
		this.offsetY = offsetY;
	}

	public void setOffsetZ(double offsetZ) {
		this.offsetZ = offsetZ;
	}

	public HMC5883L(int busNumber, int address) throws IOException {
		I2CBus bus = I2CFactory.getInstance(busNumber);
		device = bus.getDevice(address);

		device.write("0b01110000".getBytes(), 0, "0b01110000".length());
		device.write("0b00100000".getBytes(), 1, "0b01110000".length());
		device.write("0b00000000".getBytes(), 2, "0b01110000".length());
	}

	/**
	 * Get the bearing value in degrees
	 * 
	 * @return
	 * @throws IOException
	 */
	public double getBearing() throws IOException {

		RawValues rawValues = getRawValues();

		double x = rawValues.getX();
		double y = rawValues.getY();

		// Apply offsets
		x = x - offsetX;
		if (flipY) {
			y = -y + offsetY;
		} else {
			y = y - offsetY;
		}

		// Calculate bearing in radians
		double bearing = Math.atan2(y, x);
		if (bearing < 0) {
			bearing += 2 * Math.PI;
		}
		// Convert to degrees
		bearing = (bearing * 180) / Math.PI;

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Bearing: " + bearing);
		}

		return bearing;
	}

	public RawValues getRawValues() throws IOException {
		double x = read(3, device) * scale;
		double y = read(7, device) * scale;
		double z = read(5, device) * scale;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("x: " + x + " - y: " + y + " - z: " + z);
		}
		return new RawValues(x, y, z);
	}

	public void calibrate() throws IOException {

		double maxX, maxY, minX, minY, minZ, maxZ;

		// Init
		offsetX = offsetY = offsetZ = 0;

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Please keep rotating the device 360° on all axes...");
		}

		// Init
		RawValues rawValues = getRawValues();
		minX = rawValues.getX();
		maxX = rawValues.getX();
		minY = rawValues.getY();
		maxY = rawValues.getY();
		minZ = rawValues.getZ();
		maxZ = rawValues.getZ();

		for (int i = 0; i < 200; i++) {

			rawValues = getRawValues();

			double x = rawValues.getX();
			double y = rawValues.getY();
			double z = rawValues.getZ();

			if (x > maxX) {
				maxX = x;
			}

			if (x < minX) {
				minX = x;
			}

			if (y > maxY) {
				maxY = y;
			}

			if (y < minY) {
				minY = y;
			}

			if (z > maxZ) {
				maxZ = z;
			}

			if (z < minZ) {
				minZ = z;
			}

			offsetX = (maxX + minX) / 2;
			offsetY = (maxY + minY) / 2;
			offsetZ = (maxZ + minZ) / 2;

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
			}
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("X: (MIN: " + minX + ", MAX: " + maxX + ") - " + "Y: (MIN: " + minY + ", MAX: " + maxY + ")"
					+ "Z: (MIN: " + minZ + ", MAX: " + maxZ + ")");
			LOGGER.info("offsetX: " + offsetX + " - offsetY: " + offsetY + " - offsetZ: " + offsetZ);
		}
	}

	private double read(int add, I2CDevice i2c) throws IOException {
		int val1 = i2c.read(add);
		int val2 = i2c.read(add + 1);
		int val = (val1 << 8) + val2;

		if (val >= 0x8000) {
			return -((65535 - val) + 1);
		} else {
			return val;
		}
	}
}
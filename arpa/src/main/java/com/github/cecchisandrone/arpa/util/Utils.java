package com.github.cecchisandrone.arpa.util;

import java.util.Random;

public class Utils {

	public static double getRandom(double min, double max) {
		Random r = new Random();
		return (r.nextDouble() * (max - min)) + min;
	}

	public static double rad2deg(double rad) {
		return (rad * 180) / Math.PI;
	}
}

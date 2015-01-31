package com.github.cecchisandrone.arpa.util;

import java.util.Random;

public class Utils {

	public static double getRandom(double min, double max) {
		Random r = new Random();
		return (r.nextDouble() * (max - min)) + min;
	}
}

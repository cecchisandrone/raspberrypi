package com.github.cecchisandrone.arpa.launcher;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ArpaLauncher {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"application-context.xml");

	}

}

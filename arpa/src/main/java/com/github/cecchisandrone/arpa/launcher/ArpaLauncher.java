package com.github.cecchisandrone.arpa.launcher;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.cecchisandrone.arpa.module.AbstractAgentModule;
import com.github.cecchisandrone.arpa.module.ModuleContainer;

public class ArpaLauncher {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"application-context.xml");
		applicationContext.registerShutdownHook();

		ModuleContainer moduleContainer = applicationContext.getBean(ModuleContainer.class);

		while (true) {

			if (moduleContainer.getModules().size() == 0) {
				break;
			}

			for (AbstractAgentModule module : moduleContainer.getModules()) {
				module.executeWork();
			}
		}
	}
}

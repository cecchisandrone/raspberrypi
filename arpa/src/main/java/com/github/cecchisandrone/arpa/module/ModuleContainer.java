package com.github.cecchisandrone.arpa.module;

import java.util.List;

public class ModuleContainer {

	private List<AbstractAgentModule> modules;

	public void setModules(List<AbstractAgentModule> modules) {
		this.modules = modules;
	}

	public List<AbstractAgentModule> getModules() {
		return modules;
	}

	public void initializeModules() {
		for (AbstractAgentModule module : modules) {
			module.init();
			module.start();
		}
	}
}

package com.github.cecchisandrone.arpa.module;

import java.util.Collections;
import java.util.Comparator;
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
		Collections.sort(modules, new Comparator<AbstractAgentModule>() {
			@Override
			public int compare(AbstractAgentModule o1, AbstractAgentModule o2) {
				return o1.getRunPriority() - o2.getRunPriority();
			}
		});
	}
}

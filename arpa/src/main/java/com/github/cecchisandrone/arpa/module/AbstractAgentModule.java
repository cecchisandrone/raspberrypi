package com.github.cecchisandrone.arpa.module;

public abstract class AbstractAgentModule {

	private int runPriority;

	public int getRunPriority() {
		return runPriority;
	}

	public void setRunPriority(int runPriority) {
		this.runPriority = runPriority;
	}

	public abstract void executeWork();
}

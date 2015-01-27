package com.github.cecchisandrone.arpa.module;

public abstract class AbstractAgentModule extends Thread {

	@Override
	public void run() {
		executeWork();
	}

	abstract void executeWork();
}

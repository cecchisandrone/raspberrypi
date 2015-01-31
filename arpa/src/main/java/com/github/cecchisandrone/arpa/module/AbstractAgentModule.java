package com.github.cecchisandrone.arpa.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAgentModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAgentModule.class);

	private boolean terminated = false;

	private Thread thread;

	private int priority;

	public void setPriority(int priority) {

		if (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY) {
			throw new IllegalArgumentException("Priority should be <= 10 and >=0");
		}

		this.priority = priority;
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}

	public boolean isTerminated() {
		return terminated;
	}

	public void init() {
		thread = new Thread() {
			public void run() {
				while (!terminated) {
					executeWork();

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						LOGGER.error(e.toString(), e);
					}

				}
			}
		};
		thread.setPriority(priority);
	}

	public void start() {
		thread.start();
	}

	public void stop() {
		terminated = true;
	}

	protected abstract void executeWork();
}

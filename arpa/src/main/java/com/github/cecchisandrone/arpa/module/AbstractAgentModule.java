package com.github.cecchisandrone.arpa.module;

public abstract class AbstractAgentModule {

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
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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

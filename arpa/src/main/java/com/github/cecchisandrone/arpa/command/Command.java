package com.github.cecchisandrone.arpa.command;

import com.github.cecchisandrone.vc.wit.Outcome;

public interface Command {

	public void execute(Outcome outcome);
}

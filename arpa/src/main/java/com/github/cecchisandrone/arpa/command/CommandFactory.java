package com.github.cecchisandrone.arpa.command;

public interface CommandFactory {

	Command getCommand(String intent);
}

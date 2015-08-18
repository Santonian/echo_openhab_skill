package de.openhabskill.service;

import java.util.Arrays;
import java.util.List;

public class CommandAlternative {
	private String command;

	private List<String> alternatives;

	public CommandAlternative(final String command, final String[] alternatives) {
		this.command = command;
		this.alternatives = Arrays.asList(alternatives);
	}

	public String getCommand() {
		return command;
	}

	public boolean isResponsible(final String value) {
		return alternatives.contains(value);
	}
}

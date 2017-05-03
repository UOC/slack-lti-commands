package edu.uoc.elc.slack.lti.entity;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public enum Command {
	HELP;


	public static Command fromRequest(CommandRequest request) {
		String[] command = request.getText().split(" ");
		return Command.valueOf(command[0].toUpperCase());
	}
}

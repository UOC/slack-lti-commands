package edu.uoc.elc.slack.lti.entity;

import edu.uoc.elc.slack.lti.command.Command;
import edu.uoc.elc.slack.lti.command.DefaultCommand;
import edu.uoc.elc.slack.lti.command.HelpCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@AllArgsConstructor
public enum CommandEnum {
	HELP(new HelpCommand()),

	DEFAULT(new DefaultCommand());


	private Command command;

	public static CommandEnum fromRequest(CommandRequest request) {
		String[] commandText = request.getText().split(" ");
		try {
			return CommandEnum.valueOf(commandText[0].toUpperCase());
		} catch (IllegalArgumentException commandInvalid) {
			return DEFAULT;
		}
	}
}

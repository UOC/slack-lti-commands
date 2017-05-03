package edu.uoc.elc.slack.lti.command;

import edu.uoc.elc.slack.lti.entity.CommandRequest;
import edu.uoc.elc.slack.lti.entity.CommandResponse;
import edu.uoc.elc.slack.lti.entity.ResponseType;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class HelpCommand implements Command {
	private static String USAGE = "LTI commands. Usage:\n"
					+ "/lti help. Gets this message";

	@Override
	public CommandResponse execute(CommandRequest request) {
		return CommandResponse.builder()
						.response_type(ResponseType.EPHEMERAL.getText())
						.text(USAGE)
						.build();
	}
}

package edu.uoc.elc.slack.lti.command;

import edu.uoc.elc.slack.lti.entity.CommandRequest;
import edu.uoc.elc.slack.lti.entity.CommandResponse;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class DefaultCommand extends HelpCommand {
	private static String MESSAGE = "Parameters not valid. Some help below\n";

	@Override
	public CommandResponse execute(CommandRequest request) {
		CommandResponse response = super.execute(request);
		// add error message before the usage text
		response.setText(MESSAGE + response.getText());
		return response;
	}
}

package edu.uoc.elc.slack.lti.command;

import edu.uoc.elc.slack.lti.type.CommandRequest;
import edu.uoc.elc.slack.lti.type.CommandResponse;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public interface Command {
	public CommandResponse execute(CommandRequest request);
}

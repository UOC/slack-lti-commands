package edu.uoc.elc.slack.lti.command;

import edu.uoc.elc.slack.lti.entity.CommandRequest;
import edu.uoc.elc.slack.lti.entity.CommandResponse;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public interface Command {
	public CommandResponse execute(CommandRequest request);
}

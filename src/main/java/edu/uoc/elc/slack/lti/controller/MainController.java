package edu.uoc.elc.slack.lti.controller;

import edu.uoc.elc.slack.lti.entity.CommandEnum;
import edu.uoc.elc.slack.lti.entity.CommandRequest;
import edu.uoc.elc.slack.lti.entity.CommandResponse;
import edu.uoc.elc.slack.lti.entity.ResponseType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RestController
@RequestMapping("/lti")
public class MainController {

	@RequestMapping(method = RequestMethod.POST)
	public CommandResponse ltiCommand(CommandRequest request) {
		CommandEnum command = CommandEnum.fromRequest(request);
		return command.getCommand().execute(request);
	}
}

package edu.uoc.elc.slack.lti.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Example from {@see https://api.slack.com/slash-commands}
 * <code>
 * 	token=gIkuvaNzQIHg97ATvDxqgjtO
 * 	team_id=T0001
 * 	team_domain=example
 * 	channel_id=C2147483705
 * 	channel_name=test
 * 	user_id=U2147483697
 * 	user_name=Steve
 * 	command=/weather
 * 	text=94070
 *	response_url=https://hooks.slack.com/commands/1234/5678
 * </code>
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
@ToString
public class CommandRequest {

	private String token;
	private String team_id;
	private String team_domain;
	private String channel_id;
	private String channel_name;
	private String user_id;
	private String user_name;
	private String command;
	private String text;
	private String response_url;

}

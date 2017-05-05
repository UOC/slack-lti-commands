/*
 *     Slack LTI Commands - Classes to provide LTI consumer options to Slack
 *     Copyright (c) 2017  $author
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation; either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *     Contact: xaracil@uoc.edu
 *
 *
 */

package edu.uoc.elc.slack.lti.type;

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

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

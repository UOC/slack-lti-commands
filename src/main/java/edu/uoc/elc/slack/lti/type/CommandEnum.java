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

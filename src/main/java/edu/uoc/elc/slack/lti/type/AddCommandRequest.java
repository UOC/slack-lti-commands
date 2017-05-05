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

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
public class AddCommandRequest {
	private final static int REQUIRED_FIELDS = 5;
	private final static int ALIAS_IDX = 1;
	private final static int KEY_IDX = 2;
	private final static int SECRET_IDX = 3;
	private final static int URL_IDX = 4;

	private String alias;
	private String consumerKey;
	private String consumerSecret;
	private String launchUrl;
	private String description;
	private boolean valid;
	private CommandRequest request;

	public AddCommandRequest(CommandRequest request) {
		this.request = request;
		String[] text = request.getText().split("\\s+");
		this.valid = text.length >= REQUIRED_FIELDS;
		if (valid) {
			this.alias = text[ALIAS_IDX];
			this.consumerKey = text[KEY_IDX];
			this.consumerSecret = text[SECRET_IDX];
			this.launchUrl = text[URL_IDX];
			this.description = Arrays.asList(text).subList(REQUIRED_FIELDS, text.length).stream().collect(Collectors.joining(" "));
		}
	}
}

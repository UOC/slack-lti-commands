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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
public class SetCustomParametersCommandRequest {

	private final static int REQUIRED_FIELDS = 1;
	private final static int ALIAS_IDX = 1;

	private String alias;
	private String custom_parameters;
	private boolean valid;
	private CommandRequest request;
	private String customParameterWithError = null;

	public SetCustomParametersCommandRequest(CommandRequest request) {
		this.request = request;
		String[] text = request.getText().split("\\s+");
		this.valid = text.length >= REQUIRED_FIELDS;
		if (valid) {
			this.alias = text[ALIAS_IDX];
            List<String> paramList =  Arrays.asList(text).subList(2, text.length);
            String pattern = "(\\S+)=(\\S*)(?:\\b(?!=)|$)";
            Pattern p = Pattern.compile(pattern);

            custom_parameters = "";
            for(String param: paramList) {
                Matcher m = p.matcher(param);
                if (m.matches()) {
                    custom_parameters += (custom_parameters.length()>0?" ":"")+m.group(1)+"="+m.group(2);
                } else {
                    this.valid = false;
                    customParameterWithError = param;
                    break;
                }
            }

		}
	}
}

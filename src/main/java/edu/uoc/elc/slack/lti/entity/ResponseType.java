package edu.uoc.elc.slack.lti.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@AllArgsConstructor
public enum ResponseType {
	INCHANNEL("in_channel"),
	EPHEMERAL("ephemeral");

	private String text;
}

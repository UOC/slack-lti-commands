package edu.uoc.elc.slack.lti.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandResponse {
	private String text;
	private String response_type;
}

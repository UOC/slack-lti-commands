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

package edu.uoc.elc.slack.lti.form;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
@Builder
public class ConsumerLaunch {

	private String action;
	private String oauth_signature;
	private String oauth_nonce;
	private String lti_version;
	private String oauth_timestamp;

	private String lis_person_name_full;
	private String lis_person_name_family;
	private String lis_person_name_given;
	private String lis_person_sourceid;
	private String user_id;
	private String roles;
	private String resource_link_id;
	private String resource_link_title;
	private String resource_link_description;
	private String context_id;
	private String context_label;
	private String context_title;
	private String tool_consumer_info_product_family_code;
	private String tool_consumer_info_version;
	private String tool_consumer_instance_guid;
	private String tool_consumer_instance_description;
}

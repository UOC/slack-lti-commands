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

package edu.uoc.elc.slack.lti.dto;

import allbegray.slack.type.Authentication;
import allbegray.slack.type.User;
import edu.uoc.elc.slack.lti.entity.ChannelConsumer;
import org.oscelot.lti.tp.ToolConsumer;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class LtiConsumerPropertiesFactory {

	private String or(String lhs, String rhs) {
		return StringUtils.isEmpty(lhs) ? rhs : lhs;
	}

	public Set<Map.Entry<String,String>> paramsForLaunch(ChannelConsumer channelConsumer, Authentication slackAuth, User user, boolean current_user_is_creator) {
		HashSet<Map.Entry<String,String>> httpParams = new HashSet<Map.Entry<String,String>>();

		Map<String, String> propertiesMap = new TreeMap<>();

		propertiesMap.put("lis_person_name_full", or(user.getProfile().getReal_name(), slackAuth.getUser()));
		propertiesMap.put("lis_person_name_family", or(user.getProfile().getLast_name(), slackAuth.getUser()));
		propertiesMap.put("lis_person_name_given", or(user.getProfile().getFirst_name(), slackAuth.getUser()));
		propertiesMap.put("lis_person_sourceid", channelConsumer.getChannelId() + ":" + slackAuth.getUser_id());
		propertiesMap.put("user_id", slackAuth.getUser_id());
		propertiesMap.put("roles", current_user_is_creator?"Administrator":"Learner"); // TODO: add operation to create teachers
		propertiesMap.put("resource_link_id", channelConsumer.getChannelId() + ":" + channelConsumer.getAlias());
		propertiesMap.put("resource_link_title", channelConsumer.getAlias());
		propertiesMap.put("resource_link_description", channelConsumer.getDescription());
		propertiesMap.put("context_id", channelConsumer.getChannelId());
		propertiesMap.put("context_label", channelConsumer.getChannelName());
		propertiesMap.put("context_title", channelConsumer.getChannelName());
		propertiesMap.put("tool_consumer_info_product_family_code", "slack-lti");
		propertiesMap.put("tool_consumer_info_version", "0.0.1");
		propertiesMap.put("tool_consumer_instance_guid", channelConsumer.getTeamId() + ".slack.com");
		propertiesMap.put("tool_consumer_instance_description", channelConsumer.getTeamDomain());

		if (channelConsumer.getCustomParameters()!=null) {
			String[] params = channelConsumer.getCustomParameters().split(" ");
			for(String custom: params) {
				String [] customParam = custom.split("=");
				if (customParam.length==2) {
					propertiesMap.put("custom_" + customParam[0], customParam[1]);
				}
			}
		}

		propertiesMap.put("lti_version", "LTI-1p0");
		propertiesMap.put("lti_message_type", "basic-lti-launch-request");

		httpParams.addAll(propertiesMap.entrySet());
		return httpParams;
	}
}

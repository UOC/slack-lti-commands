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

import edu.uoc.elc.slack.lti.entity.ChannelConsumer;
import edu.uoc.elc.slack.lti.form.ConsumerLaunch;
import org.oscelot.lti.tp.ToolConsumer;

import java.util.*;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class LtiConsumerPropertiesFactory {

	public Set<Map.Entry<String,String>> paramsForLaunch(ChannelConsumer channelConsumer, ToolConsumer tc, String userName, String userId) {
		HashSet<Map.Entry<String,String>> httpParams = new HashSet<Map.Entry<String,String>>();

		Map<String, String> propertiesMap = new TreeMap<>();

		propertiesMap.put("lis_person_name_full", userName);
		propertiesMap.put("lis_person_name_family", userName);
		propertiesMap.put("lis_person_name_given", userName);
		propertiesMap.put("lis_person_sourceid", channelConsumer.getChannelId() + ":" + userId);
		propertiesMap.put("user_id", userId);
		propertiesMap.put("roles", "Learner"); // TODO: check
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

		propertiesMap.put("lti_version", "LTI-2p0");
		propertiesMap.put("lti_message_type", "basic-lti-launch-request");

		httpParams.addAll(propertiesMap.entrySet());
		return httpParams;
	}

	public ConsumerLaunch newConsumerLaunch(ChannelConsumer channelConsumer, ToolConsumer tc, String userName, String userId, String channelName, String slackApp, String slackName) {


		return ConsumerLaunch.builder()
						.lis_person_name_full(userName)
						.lis_person_name_family(userName)
						.lis_person_name_given(userName)
						.lis_person_sourceid(channelConsumer.getChannelId() + ":" + userId)
						.user_id(userId)
						.roles("Learner") // TODO: check
						.resource_link_id(channelConsumer.getChannelId() + ":" + channelConsumer.getAlias())
						.resource_link_title(channelConsumer.getAlias())
						.resource_link_description(channelConsumer.getDescription())
						.context_id(channelConsumer.getChannelId())
						.context_label(channelName)
						.context_title(channelName)
						.tool_consumer_info_product_family_code("slack-lti")
						.tool_consumer_info_version("0.0.1")
						.tool_consumer_instance_guid(slackApp)
						.tool_consumer_instance_description(slackName)
						.build();
	}
}

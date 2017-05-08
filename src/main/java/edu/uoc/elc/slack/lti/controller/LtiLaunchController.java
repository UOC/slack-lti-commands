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

package edu.uoc.elc.slack.lti.controller;

import edu.uoc.elc.slack.lti.controller.exception.ChannelConsumerNotAvailableException;
import edu.uoc.elc.slack.lti.controller.exception.ChannelConsumerNotEnabledException;
import edu.uoc.elc.slack.lti.controller.exception.ChannelConsumerNotFoundException;
import edu.uoc.elc.slack.lti.dto.LtiConsumerPropertiesFactory;
import edu.uoc.elc.slack.lti.entity.ChannelConsumer;
import edu.uoc.elc.slack.lti.entity.ChannelConsumerId;
import edu.uoc.elc.slack.lti.repository.ChannelConsumerRepository;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import org.oscelot.lti.tp.DataConnector;
import org.oscelot.lti.tp.ToolConsumer;
import org.oscelot.lti.tp.dataconnector.JDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Controller
public class LtiLaunchController {

	@Autowired
	private ChannelConsumerRepository channelConsumerRepository;

	@Autowired
	private DataSource dataSource;

	@RequestMapping("/launch/{channelId}/{alias}")
	public String launch(@PathVariable("channelId") String channelId, @PathVariable("alias") String alias, Model model) throws Throwable {
		ChannelConsumerId channelConsumerId = new ChannelConsumerId(channelId, alias);
		final ChannelConsumer channelConsumer = channelConsumerRepository.findOne(channelConsumerId);
		if (channelConsumer == null) {
			throw new ChannelConsumerNotFoundException();
		}

		DataConnector dataConnector = new JDBC("", dataSource.getConnection());
		ToolConsumer tc = new ToolConsumer(channelConsumer.getToolConsumerKey(), dataConnector, false);
		if (!tc.isAvailable()) {
			throw new ChannelConsumerNotAvailableException();
		}
		if (!tc.isEnabled()) {
			throw new ChannelConsumerNotEnabledException();
		}

		// TODO: get from Slack
		String userName = "test user";
		String userId = "12345";
		String channelName = "test channel";
		String slackApp = "elearnlab.slack.com";
		String slackName = "eLearn Lab";

		// prepare launch
		LtiConsumerPropertiesFactory ltiConsumerPropertiesFactory = new LtiConsumerPropertiesFactory();

		List<Map.Entry<String, String>> reqParams = new ArrayList<Map.Entry<String, String>>();
		OAuthMessage oAuthMessage = new OAuthMessage("POST", channelConsumer.getLaunchUrl(),
						ltiConsumerPropertiesFactory.paramsForLaunch(channelConsumer, tc, userName, userId, channelName, slackApp, slackName));
		OAuthConsumer oAuthConsumer = new OAuthConsumer("about:blank", channelConsumer.getConsumerKey(), tc.getSecret(), null);
		OAuthAccessor oAuthAccessor = new OAuthAccessor(oAuthConsumer);
		try {
			oAuthMessage.addRequiredParameters(oAuthAccessor);
			reqParams.addAll(oAuthMessage.getParameters());
		} catch (OAuthException e) {
		} catch (URISyntaxException e) {
		} catch (IOException e) {
		}

		//model.addAttribute("launchObj", ltiConsumerPropertiesFactory.newConsumerLaunch(reqParams));
		model.addAttribute("action", channelConsumer.getLaunchUrl());
		model.addAttribute("launch", reqParams);
		return "launch";
	}
}

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

import allbegray.slack.SlackClientFactory;
import allbegray.slack.exception.SlackResponseErrorException;
import allbegray.slack.type.Channel;
import allbegray.slack.type.User;
import allbegray.slack.webapi.SlackWebApiClient;
import edu.uoc.elc.slack.lti.controller.exception.ChannelConsumerNotAvailableException;
import edu.uoc.elc.slack.lti.controller.exception.ChannelConsumerNotEnabledException;
import edu.uoc.elc.slack.lti.controller.exception.ChannelConsumerNotFoundException;
import edu.uoc.elc.slack.lti.controller.exception.InvalidLaunchException;
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
import org.springframework.security.oauth2.client.OAuth2ClientContext;
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
import java.util.function.Predicate;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Controller
public class LtiLaunchController {

	@Autowired
	private ChannelConsumerRepository channelConsumerRepository;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private OAuth2ClientContext oauth2Context;

	private void checkCanLaunch(ToolConsumer tc, allbegray.slack.type.Authentication slackAuth, SlackWebApiClient slackWebApiClient, ChannelConsumer channelConsumer, String teamId, String channelId) {
		// check tool consumer is available
		if (!tc.isAvailable()) {
			throw new ChannelConsumerNotAvailableException();
		}
		// check tool consumer is enabled
		if (!tc.isEnabled()) {
			throw new ChannelConsumerNotEnabledException();
		}

		// check auth is from the same team
		if (slackAuth == null || !slackAuth.getTeam_id().equals(channelConsumer.getTeamId())) {
			throw new InvalidLaunchException();
		}
		// check user has access to the channel
		boolean channel_exist = false;
		try {
			channel_exist = slackWebApiClient.getChannelInfo(channelConsumer.getChannelId())!=null;
		} catch (SlackResponseErrorException sre) {
			//then try if is a private channel
			try {
				channel_exist = slackWebApiClient.getGroupInfo(channelConsumer.getChannelId())!=null;
			} catch (SlackResponseErrorException Gre) {
				//then this group / channel doesn't exist!
			}
		}
		if (!channel_exist) {
			throw new InvalidLaunchException();
		}
	}

	@RequestMapping("/launch/{teamId}/{channelId}/{alias}")
	public String launch(@PathVariable("teamId") String teamId, @PathVariable("channelId") String channelId, @PathVariable("alias") String alias, Model model) throws Throwable {
		ChannelConsumerId channelConsumerId = new ChannelConsumerId(channelId, alias);
		final ChannelConsumer channelConsumer = channelConsumerRepository.findOne(channelConsumerId);
		if (channelConsumer == null || !teamId.equals(channelConsumer.getTeamId())) {
			throw new ChannelConsumerNotFoundException();
		}

		DataConnector dataConnector = new JDBC(channelId.toLowerCase() + "_", dataSource.getConnection());
		ToolConsumer tc = new ToolConsumer(channelConsumer.getConsumerKey(), dataConnector, false);

		// get from Slack
		final SlackWebApiClient slackWebApiClient = SlackClientFactory.createWebApiClient(oauth2Context.getAccessToken().getValue());
		final allbegray.slack.type.Authentication slackAuth = slackWebApiClient.auth();
		final User userInfo = slackWebApiClient.getUserInfo(slackAuth.getUser_id());

		try {
			// check we can do the launch
			checkCanLaunch(tc, slackAuth, slackWebApiClient, channelConsumer, teamId, channelId);

			// prepare launch
			LtiConsumerPropertiesFactory ltiConsumerPropertiesFactory = new LtiConsumerPropertiesFactory();

			List<Map.Entry<String, String>> reqParams = new ArrayList<Map.Entry<String, String>>();
			OAuthMessage oAuthMessage = new OAuthMessage("POST", channelConsumer.getLaunchUrl(),
					ltiConsumerPropertiesFactory.paramsForLaunch(channelConsumer, slackAuth, userInfo));
			OAuthConsumer oAuthConsumer = new OAuthConsumer("about:blank", channelConsumer.getConsumerKey(), tc.getSecret(), null);
			OAuthAccessor oAuthAccessor = new OAuthAccessor(oAuthConsumer);
			try {
				oAuthMessage.addRequiredParameters(oAuthAccessor);
				reqParams.addAll(oAuthMessage.getParameters());
			} catch (OAuthException e) {
			} catch (URISyntaxException e) {
			} catch (IOException e) {
			}

			model.addAttribute("action", channelConsumer.getLaunchUrl());
			model.addAttribute("launch", reqParams);
			return "launch";
		} catch (Exception e){
			System.err.println("Getting exception Exception "+e.getMessage());
			e.printStackTrace();;
			return "error";

		}
	}
}

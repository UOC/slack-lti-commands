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

import edu.uoc.elc.slack.lti.command.ChannelConsumerRepositoryAware;
import edu.uoc.elc.slack.lti.command.Command;
import edu.uoc.elc.slack.lti.command.DataConnectorAware;
import edu.uoc.elc.slack.lti.command.ServerURLAware;
import edu.uoc.elc.slack.lti.repository.ChannelConsumerRepository;
import edu.uoc.elc.slack.lti.type.CommandEnum;
import edu.uoc.elc.slack.lti.type.CommandRequest;
import edu.uoc.elc.slack.lti.type.CommandResponse;
import org.oscelot.lti.tp.DataConnector;
import org.oscelot.lti.tp.dataconnector.JDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.net.URL;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RestController
@RequestMapping("/lti")
public class MainController {

	@Autowired
	private ChannelConsumerRepository channelConsumerRepository;

	@Autowired
	private DataSource dataSource;

	@RequestMapping(method = RequestMethod.POST)
	public CommandResponse ltiCommand(CommandRequest request, HttpServletRequest httpServletRequest) throws Throwable{
		CommandEnum commandEnum = CommandEnum.fromRequest(request);
		final Command command = commandEnum.getCommand();

		// inject beans
		if (command instanceof ChannelConsumerRepositoryAware) {
			((ChannelConsumerRepositoryAware) command).setChannelConsumerRepository(channelConsumerRepository);
		}

		if (command instanceof DataConnectorAware) {
			DataConnector dataConnector = new JDBC("", dataSource.getConnection());
			((DataConnectorAware) command).setDataConnector(dataConnector);
		}

		if (command instanceof ServerURLAware) {
			URL url = new URL(httpServletRequest.getRequestURL().toString());
			((ServerURLAware) command).setServerUrl(url.getProtocol() + "://" + url.getHost());
		}
		return command.execute(request);
	}
}

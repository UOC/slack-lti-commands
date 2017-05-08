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

package edu.uoc.elc.slack.lti.command;

import edu.uoc.elc.slack.lti.dto.ChannelConsumerIdFactory;
import edu.uoc.elc.slack.lti.entity.ChannelConsumer;
import edu.uoc.elc.slack.lti.entity.ChannelConsumerId;
import edu.uoc.elc.slack.lti.repository.ChannelConsumerRepository;
import edu.uoc.elc.slack.lti.type.*;
import lombok.Setter;
import org.oscelot.lti.tp.DataConnector;

import java.util.Arrays;
import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Setter
public class LaunchCommand implements Command, ChannelConsumerRepositoryAware, DataConnectorAware, ServerURLAware {
	private final static String ERROR_MSG = "Sorry, that didn't work. Rember the usage:\n"
					+ "/lti launch alias";
	private final static String DOESNT_EXISTS_ERROR_MSG = "Sorry, consumer with alias `%s` does not exist. Try another one.";
	private final static String SUCCESS_MSG = "<@%s|%s> launched lti consumer `%s`.";

	private ChannelConsumerRepository channelConsumerRepository;
	private DataConnector dataConnector;
	private String serverUrl;

	@Override
	public CommandResponse execute(CommandRequest request) {
		LaunchCommandRequest launchCommandRequest = new LaunchCommandRequest(request);
		if (!launchCommandRequest.isValid()) {
			return error(ERROR_MSG);
		}

		ChannelConsumerIdFactory channelConsumerIdFactory = new ChannelConsumerIdFactory();
		ChannelConsumerId channelConsumerId = channelConsumerIdFactory.newChannelConsumerId(launchCommandRequest);

		final ChannelConsumer channelConsumer = channelConsumerRepository.findOne(channelConsumerId);
		if (channelConsumer == null) {
			return error(String.format(DOESNT_EXISTS_ERROR_MSG, channelConsumerId.getAlias()));
		}

		// attachment
		Attachment attachment = Attachment.builder()
						.title(channelConsumer.getDescription())
						.title_link(getLaunchLink(channelConsumerId))
						.text("Click on link to launch")
						.build();

		return CommandResponse.builder()
						.response_type(ResponseType.INCHANNEL.getText())
						.text(String.format(SUCCESS_MSG, launchCommandRequest.getRequest().getUser_id(), launchCommandRequest.getRequest().getUser_name(), channelConsumer.getAlias()))
						.attachments(Arrays.asList(attachment))
						.build();
	}

	private String getLaunchLink(ChannelConsumerId channelConsumerId) {
		return serverUrl + "/launch/" + channelConsumerId.getChannelId() + "/" + channelConsumerId.getAlias();
	}

	private CommandResponse error(String message) {
		return CommandResponse.builder()
						.response_type(ResponseType.EPHEMERAL.getText())
						.text(message)
						.build();
	}
}

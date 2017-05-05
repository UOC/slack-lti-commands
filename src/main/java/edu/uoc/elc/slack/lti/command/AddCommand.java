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

import edu.uoc.elc.slack.lti.dto.ChannelConsumerFactory;
import edu.uoc.elc.slack.lti.entity.ChannelConsumer;
import edu.uoc.elc.slack.lti.entity.ChannelConsumerId;
import edu.uoc.elc.slack.lti.repository.ChannelConsumerRepository;
import edu.uoc.elc.slack.lti.type.AddCommandRequest;
import edu.uoc.elc.slack.lti.type.CommandRequest;
import edu.uoc.elc.slack.lti.type.CommandResponse;
import edu.uoc.elc.slack.lti.type.ResponseType;
import lombok.Setter;
import org.oscelot.lti.tp.DataConnector;
import org.oscelot.lti.tp.ToolConsumer;
import org.oscelot.lti.tp.dataconnector.JDBC;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Setter
public class AddCommand implements Command, ChannelConsumerRepositoryAware, DataConnectorAware {

	private final static String ERROR_MSG = "Sorry, that didn't work. Rember the usage:\n"
					+ "/lti add alias key secret launch_url description";
	private final static String EXISTS_ERROR_MSG = "Sorry, consumer with alias `%s` already exists. Try another one.";
	private final static String SUCCESS_MSG = "consumer with alias `%s` added.";

	private ChannelConsumerRepository channelConsumerRepository;
	private DataConnector dataConnector;

	private CommandResponse error(String message) {
		return CommandResponse.builder()
						.response_type(ResponseType.EPHEMERAL.getText())
						.text(message)
						.build();
	}

	@Override
	public CommandResponse execute(CommandRequest request) {
		AddCommandRequest addCommandRequest = new AddCommandRequest(request);
		if (!addCommandRequest.isValid()) {
			return error(ERROR_MSG);
		}


		ChannelConsumerFactory channelConsumerFactory = new ChannelConsumerFactory();

		final ChannelConsumer channelConsumer = channelConsumerFactory.newChannelConsumer(addCommandRequest);

		// check if alias already exists
		ChannelConsumerId channelConsumerId = new ChannelConsumerId(channelConsumer.getChannelId(), channelConsumer.getAlias());
		if (channelConsumerRepository.exists(channelConsumerId)) {
			return error(String.format(EXISTS_ERROR_MSG, channelConsumer.getAlias()));
		}

		final ChannelConsumer saved = channelConsumerRepository.save(channelConsumer);
		if (saved != null) {
			// add the consumer
			ToolConsumer tc = new ToolConsumer(getToolConsumerKey(addCommandRequest), dataConnector, true);
			tc.setName(addCommandRequest.getAlias());
			tc.setSecret(addCommandRequest.getConsumerSecret());

			if (!tc.save()) {
				return error(ERROR_MSG);
			}
		}

		return CommandResponse.builder()
						.response_type(ResponseType.EPHEMERAL.getText())
						.text(String.format(SUCCESS_MSG, saved.getAlias()))
						.build();
	}

	private String getToolConsumerKey(AddCommandRequest addCommandRequest) {
		return addCommandRequest.getRequest().getChannel_id() + ":" + addCommandRequest.getConsumerKey();
	}
}

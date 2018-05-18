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
import edu.uoc.elc.slack.lti.type.*;
import lombok.Setter;
import org.oscelot.lti.tp.DataConnector;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Setter
public class SetCustomParametersCommand implements Command, ChannelConsumerRepositoryAware {

	private final static String ERROR_MSG = "Sorry, that didn't work. Remember the usage:\n"
					+ "/lti custom_params alias custom_param1=value1 custom_param2=value2";
	private final static String CUSTOM_ERROR_MSG = "Sorry, there is an error in custom parameters.";
	private final static String CUSTOM_ERROR_MSG_CUSTOM = "Sorry, there is an error in custom parameters. review this `%s` parameter";
	private final static String ERROR_ALIAS_NOT_EXIST = "Sorry, the alias `%s` doesn't exist";
	private final static String SUCCESS_MSG = "custom parameters are save to  alias `%s`.";

	private ChannelConsumerRepository channelConsumerRepository;

	@Override
	public CommandResponse execute(CommandRequest request) {
		SetCustomParametersCommandRequest setCustomParametersCommandRequest = new SetCustomParametersCommandRequest(request);
		if (!setCustomParametersCommandRequest.isValid()) {
			return error(
					setCustomParametersCommandRequest.getCustomParameterWithError()!=null?
							CUSTOM_ERROR_MSG:
							String.format(CUSTOM_ERROR_MSG_CUSTOM, setCustomParametersCommandRequest.getCustomParameterWithError()));
		}

		// check if alias exists
		ChannelConsumerId channelConsumerId = new ChannelConsumerId(setCustomParametersCommandRequest.getRequest().getChannel_id(),
				setCustomParametersCommandRequest.getAlias());
		if (!channelConsumerRepository.exists(channelConsumerId)) {
			return error(String.format(ERROR_ALIAS_NOT_EXIST, setCustomParametersCommandRequest.getAlias()));
		}

		ChannelConsumer channelConsumer = channelConsumerRepository.findOne(channelConsumerId);
		channelConsumer.setCustomParameters(setCustomParametersCommandRequest.getCustom_parameters());

		final ChannelConsumer saved = channelConsumerRepository.save(channelConsumer);


		return CommandResponse.builder()
						.response_type(ResponseType.EPHEMERAL.getText())
						.text(String.format(SUCCESS_MSG, saved.getAlias()))
						.build();
	}


	private CommandResponse error(String message) {
		return CommandResponse.builder()
						.response_type(ResponseType.EPHEMERAL.getText())
						.text(message)
						.build();
	}
}

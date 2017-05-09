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

package edu.uoc.elc.slack.lti.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class LtiConsumerRepository {
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	private final static String LTI_CONSUMER_TABLE_SQL = "_lti_consumer (\n" +
					"  consumer_key varchar(255) NOT NULL,\n" +
					"  name varchar(45) NOT NULL,\n" +
					"  secret varchar(32) NOT NULL,\n" +
					"  lti_version varchar(12) DEFAULT NULL,\n" +
					"  consumer_name varchar(255) DEFAULT NULL,\n" +
					"  consumer_version varchar(255) DEFAULT NULL,\n" +
					"  consumer_guid varchar(255) DEFAULT NULL,\n" +
					"  css_path varchar(255) DEFAULT NULL,\n" +
					"  protected tinyint(1) NOT NULL,\n" +
					"  enabled tinyint(1) NOT NULL,\n" +
					"  enable_from datetime DEFAULT NULL,\n" +
					"  enable_until datetime DEFAULT NULL,\n" +
					"  last_access date DEFAULT NULL,\n" +
					"  created datetime NOT NULL,\n" +
					"  updated datetime NOT NULL,\n" +
					"  PRIMARY KEY (consumer_key)\n" +
					") ENGINE=InnoDB DEFAULT CHARSET=latin1;";


	public LtiConsumerRepository(DataSource dataSource, JdbcTemplate jdbcTemplate) {
		this.dataSource = dataSource;
		this.jdbcTemplate = jdbcTemplate;
	}

	private boolean tableForChannelExists(String channelId) throws SQLException {
		final DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
		final ResultSet tables = metaData.getTables(null, null, channelId + "_lti_consumer", null);
		return tables.next();
	}

	public void createConsumerTableForChannel(String channelId) throws SQLException {
		final String normalizedChannelId = channelId.toLowerCase();
		if (!tableForChannelExists(normalizedChannelId)) {
			// create table
			jdbcTemplate.execute("CREATE TABLE " + normalizedChannelId + LTI_CONSUMER_TABLE_SQL);
		}
	}

}

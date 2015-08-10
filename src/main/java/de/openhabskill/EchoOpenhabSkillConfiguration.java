package de.openhabskill;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

public class EchoOpenhabSkillConfiguration extends Configuration {
	@Valid
	@NotNull
	private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();

	@Valid
	@NotNull
	private DatabaseConfiguration database = new DatabaseConfiguration();

	@JsonProperty("httpClient")
	public JerseyClientConfiguration getJerseyClientConfiguration() {
		return httpClient;
	}

	@JsonProperty("httpClient")
	public void setJerseyClientConfiguration(JerseyClientConfiguration httpClient) {
		this.httpClient = httpClient;
	}

	@JsonProperty("database")
	public DatabaseConfiguration getDatabase() {
		return database;
	}

	@JsonProperty("database")
	public void setDatabase(DatabaseConfiguration database) {
		this.database = database;
	}

}

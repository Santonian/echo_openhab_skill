package de.openhabskill;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * configuration class for the openHab Server
 * 
 * @author Reinhard
 *
 */
public class OpenHabConfiguration {

	@NotEmpty
	private String host;

	@NotNull
	private Integer port;

	@JsonProperty
	public String getHost() {
		return host;
	}

	@JsonProperty
	public void setHost(String host) {
		this.host = host;
	}

	@JsonProperty
	public Integer getPort() {
		return port;
	}

	@JsonProperty
	public void setPort(Integer port) {
		this.port = port;
	}

}

package de.openhabskill.client;

import java.net.URI;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenHabClient {
	private static final Logger LOG = LoggerFactory.getLogger(OpenHabClient.class);

	private final String localhost;
	private final Integer port;
	private final Client httpClient;

	public OpenHabClient(final String localhost, final Integer port, final Client httpClient) {
		this.httpClient = httpClient;
		this.localhost = localhost;
		this.port = port;
	}

	public boolean sendCommand(final String openHabItemName, final String command) {
		final WebTarget target = httpClient.target(getBaseURI());

		final Response response = target.path(openHabItemName).request().post(Entity.text(command.toUpperCase()));

		return response.getStatusInfo().getFamily().equals(Status.Family.SUCCESSFUL);
	}

	public String getState(final String openHabItemName) {
		final WebTarget target = httpClient.target(getBaseURI());

		String state = "";
		try {
			state = target.path(openHabItemName).path("state").request().get(String.class);
		} catch (WebApplicationException e) {
			LOG.error("getState request returned with " + e.getResponse().getStatus(), e);
		}

		return state;

	}

	private URI getBaseURI() {
		return UriBuilder.fromUri(String.format("http://%s:%d/rest/items", localhost, port)).build();
	}
}

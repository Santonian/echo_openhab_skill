package de.openhabskill.client;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.junit.Assert;

public class OpenHabClient {
	private final String localhost;
	private final Integer port;
	private final Client httpClient;

	public OpenHabClient(final String localhost, final Integer port, final Client httpClient) {
		this.httpClient = httpClient;
		this.localhost = localhost;
		this.port = port;
	}

	public void callOpenHab(final String onOff) {

		WebTarget target = httpClient.target(getBaseURI());

		Response response = target.path("Licht_EG_Buero_Decke").request().post(Entity.text(onOff));

		Assert.assertTrue(response.getStatusInfo().getFamily().equals(Status.Family.SUCCESSFUL));

	}

	private URI getBaseURI() {
		return UriBuilder.fromUri(String.format("http://%s:%d/rest/items", localhost, port)).build();
	}
}

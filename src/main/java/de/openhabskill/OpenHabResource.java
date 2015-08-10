package de.openhabskill;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

import de.openhabskill.speechlet.OpenHabSpeechlet;

@Path("/openhab")
@Produces(MediaType.APPLICATION_JSON)
public class OpenHabResource extends SpeechletServlet {
	private static final long serialVersionUID = 1L;

	@Context
	private HttpServletRequest servletRequest;

	@Context
	HttpServletResponse servletResponse;

	@POST
	public void postWrapper() throws ServletException, IOException {
		super.doPost(servletRequest, servletResponse);
	}

	public OpenHabResource(final OpenHabClient openHabClient) {
		setSpeechlet(new OpenHabSpeechlet(openHabClient));
	}
}

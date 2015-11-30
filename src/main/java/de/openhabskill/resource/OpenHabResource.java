package de.openhabskill.resource;

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

import de.openhabskill.client.OpenHabClient;
import de.openhabskill.entity.ItemDao;
import de.openhabskill.service.OpenHabSpeechlet;

/**
 * This is the endpoint for the amazon skill. Amazon cloud will call this URL.
 * Implements the {@link SpeechletServlet} from the ASK (Alexa SKills Kit)
 * 
 * @author Reinhard
 *
 */
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

	public OpenHabResource(final OpenHabClient openHabClient, final ItemDao itemDao) {
		setSpeechlet(new OpenHabSpeechlet(openHabClient, itemDao));
	}
}

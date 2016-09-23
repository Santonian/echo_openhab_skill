package de.openhabskill.resource;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

import de.openhabskill.service.OpenHabSpeechlet;

/**
 * This is the endpoint for the amazon skill. Amazon cloud will call this URL.
 * Implements the {@link SpeechletServlet} from the ASK (Alexa SKills Kit)
 * 
 * @author Reinhard
 *
 */

@RestController
public class AlexaSkillController extends SpeechletServlet {
	private static final long serialVersionUID = 1L;

	@Autowired
	OpenHabSpeechlet speechlet;

	@RequestMapping(path = "/alexaskill", method = RequestMethod.POST)
	public void postWrapper(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doPost(request, response);
	}

	@PostConstruct
	public void initSpeechlet() {
		setSpeechlet(speechlet);
	}
}

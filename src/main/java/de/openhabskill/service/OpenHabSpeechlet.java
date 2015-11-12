package de.openhabskill.service;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.google.common.collect.Sets;

import de.openhabskill.client.OpenHabClient;
import de.openhabskill.entity.ItemDao;

public class OpenHabSpeechlet implements Speechlet {

	private static final Logger LOG = LoggerFactory.getLogger(OpenHabSpeechlet.class);

	private Set<IntentHandler> intentHandlers = Sets.newHashSet();

	public OpenHabSpeechlet(final OpenHabClient openHabClient, final ItemDao itemDao) {
		// Initialize the Intent handler
		intentHandlers.add(new AskTemperatureIntentHandler(openHabClient, itemDao));
		intentHandlers.add(new SwitchIntentHandler(openHabClient, itemDao));
		intentHandlers.add(new TvIntentHandler(openHabClient, itemDao));
		intentHandlers.add(new RollershutterIntentHandler(openHabClient, itemDao));
		intentHandlers.add(new DimIntentHandler(openHabClient, itemDao));
		intentHandlers.add(new ColorIntentHandler(openHabClient, itemDao));
	}

	@Override
	public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
		LOG.debug("onIntent");

		final Intent intent = request.getIntent();
		final String intentName = intent.getName();

		for (IntentHandler ih : intentHandlers) {
			if (ih.isResponsible(intentName)) {
				return ih.handleIntent(intent);
			}
		}

		throw new SpeechletException("The Intent " + intentName + " is not recognized.");
	}

	@Override
	public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
		LOG.debug("onLaunch");
		return IntentHandler.buildSpeechletResponse("Yes?", false);
	}

	@Override
	public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
		LOG.debug("onSessionEnded");
		// TODO Auto-generated method stub

	}

	@Override
	public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
		LOG.debug("onSessionStarted");
		// TODO Auto-generated method stub

	}

}

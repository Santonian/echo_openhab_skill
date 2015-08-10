package de.openhabskill.speechlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

import de.openhabskill.OpenHabClient;

public class OpenHabSpeechlet implements Speechlet {

	private static final Logger LOG = LoggerFactory.getLogger(OpenHabSpeechlet.class);

	private static final String SWITCH = "SwitchLights";
	private static final String SWITCH_SWITCH = "Switch";
	private static final String SWITCH_ACTION = "Action";
	private static final String SWITCH_LOCATION = "Location";

	private static final String TEMPERATURE = "AskTemperature";
	private static final String TEMPERATURE_LOCATION = "Location";

	private static final String TV = "OperateTv";
	private static final String TV_ACTION = "Action";
	private static final String TV_CHANNEL = "Channel";

	private final OpenHabClient openHabClient;

	public OpenHabSpeechlet(final OpenHabClient openHabClient) {
		this.openHabClient = openHabClient;
	}

	@Override
	public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
		LOG.debug("onIntent");

		final String intentName = request.getIntent().getName();

		switch (intentName) {
		case SWITCH:
			return handleSwitchIntent(request.getIntent());
		case TEMPERATURE:
			return handleTemperatureIntent(request.getIntent());
		case TV:
			return handleTvIntent(request.getIntent());
		default:
			throw new SpeechletException("The Intent " + intentName + " is not recognized.");
		}
	}

	@Override
	public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
		LOG.debug("onLaunch");
		return buildSpeechletResponse("Launch openHab", "Yes?", false);
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

	private SpeechletResponse buildSpeechletResponse(final String title, final String output,
			final boolean shouldEndSession) {
		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle(String.format("OpenHabSpeechlet - %s", title));
		card.setContent(String.format("%s", output));

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(output);

		// Create the speechlet response.
		SpeechletResponse response = new SpeechletResponse();
		response.setShouldEndSession(shouldEndSession);
		response.setOutputSpeech(speech);
		response.setCard(card);
		return response;
	}

	private SpeechletResponse handleSwitchIntent(Intent intent) {
		final Slot action = intent.getSlot(SWITCH_ACTION);
		final Slot myswitch = intent.getSlot(SWITCH_SWITCH);
		final Slot location = intent.getSlot(SWITCH_LOCATION);

		// TODO: remove testcall
		openHabClient.callOpenHab(action.getValue().toUpperCase());

		return buildSpeechletResponse("Switch",
				"Ok, switching " + action.getValue() + " the " + myswitch.getValue() + " in the " + location.getValue(),
				true);
		// TODO: talk to openhab
	}

	private SpeechletResponse handleTemperatureIntent(Intent intent) {
		final Slot location = intent.getSlot(TEMPERATURE_LOCATION);

		return buildSpeechletResponse("Temperature",
				"Ok, the temperature in the " + location.getValue() + " is quite hot.", true);

		// TODO: talk to openhab
	}

	private SpeechletResponse handleTvIntent(Intent intent) {
		final Slot action = intent.getSlot(TV_ACTION);
		final Slot channel = intent.getSlot(TV_CHANNEL);

		return buildSpeechletResponse("TV", "TV is going to be operated!", true);

		// TODO: talk to openhab
	}
}

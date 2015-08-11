package de.openhabskill.service;

import org.slf4j.Logger;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

import de.openhabskill.client.OpenHabClient;
import de.openhabskill.entity.ItemDao;

/**
 * abstract base class for all intent handler
 * 
 * @author Reinhard
 *
 */
public abstract class IntentHandler {

	final protected OpenHabClient openHabClient;

	final protected ItemDao itemDao;

	public IntentHandler(final OpenHabClient openHabClient, final ItemDao itemDao) {
		this.openHabClient = openHabClient;
		this.itemDao = itemDao;

		getLogger().debug("IntentHandler created.");
	}

	public boolean isResponsible(final String intentName) {
		return getIntentName().equals(intentName);
	}

	public abstract SpeechletResponse handleIntent(final Intent intent);

	/**
	 * @return Name of the Intent this {@link IntentHandler} handles
	 */
	public abstract String getIntentName();

	protected abstract Logger getLogger();

	static public SpeechletResponse buildSpeechletResponse(final String title, final String output,
			final boolean shouldEndSession) {
		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle(String.format("OpenHabSkill - %s", title));
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

}

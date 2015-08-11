package de.openhabskill.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;

import de.openhabskill.client.OpenHabClient;
import de.openhabskill.entity.ItemDao;

public class AskTemperatureIntentHandler extends IntentHandler {
	protected static final Logger LOG = LoggerFactory.getLogger(AskTemperatureIntentHandler.class);

	private static final String TEMPERATURE = "AskTemperature";
	private static final String TEMPERATURE_LOCATION = "Location";

	public AskTemperatureIntentHandler(OpenHabClient openHabClient, ItemDao itemDao) {
		super(openHabClient, itemDao);
	}

	@Override
	public SpeechletResponse handleIntent(Intent intent) {
		final Slot location = intent.getSlot(TEMPERATURE_LOCATION);

		return buildSpeechletResponse("Temperature",
				"Ok, the temperature in the " + location.getValue() + " is quite hot.", true);
	}

	@Override
	public String getIntentName() {
		return TEMPERATURE;
	}

	@Override
	protected Logger getLogger() {
		return LOG;
	}

}

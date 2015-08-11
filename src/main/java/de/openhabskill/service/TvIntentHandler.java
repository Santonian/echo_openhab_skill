package de.openhabskill.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;

import de.openhabskill.client.OpenHabClient;
import de.openhabskill.entity.ItemDao;

public class TvIntentHandler extends IntentHandler {
	protected static final Logger LOG = LoggerFactory.getLogger(TvIntentHandler.class);

	private static final String TV = "OperateTv";
	private static final String TV_ACTION = "Action";
	private static final String TV_CHANNEL = "Channel";

	public TvIntentHandler(OpenHabClient openHabClient, ItemDao itemDao) {
		super(openHabClient, itemDao);
	}

	@Override
	public SpeechletResponse handleIntent(Intent intent) {
		final Slot action = intent.getSlot(TV_ACTION);
		final Slot channel = intent.getSlot(TV_CHANNEL);

		return buildSpeechletResponse("TV", "TV is going to be operated!", true);
	}

	@Override
	public String getIntentName() {
		return TV;
	}

	@Override
	protected Logger getLogger() {
		return LOG;
	}

}

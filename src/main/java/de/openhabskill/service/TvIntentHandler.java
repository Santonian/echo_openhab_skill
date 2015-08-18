package de.openhabskill.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.google.common.collect.Lists;

import de.openhabskill.client.OpenHabClient;
import de.openhabskill.entity.ItemDao;

public class TvIntentHandler extends IntentHandler {
	protected static final Logger LOG = LoggerFactory.getLogger(TvIntentHandler.class);

	private static final String TV = "OperateTv";

	public TvIntentHandler(OpenHabClient openHabClient, ItemDao itemDao) {
		super(openHabClient, itemDao);
	}

	@Override
	public SpeechletResponse handleIntentInternal(Intent intent) {
		final Slot action = intent.getSlot(SLOT_ACTION);
		final Slot channel = intent.getSlot(SLOT_CHANNEL);

		return buildSpeechletResponse("TV is going to be operated!", true);
	}

	@Override
	public String getIntentName() {
		return TV;
	}

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	@Override
	protected List<String> getSlotNames() {
		return Lists.newArrayList(SLOT_ACTION, SLOT_CHANNEL);
	}

}

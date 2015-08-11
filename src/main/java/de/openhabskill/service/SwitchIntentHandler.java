package de.openhabskill.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;

import de.openhabskill.client.OpenHabClient;
import de.openhabskill.entity.ItemDao;

public class SwitchIntentHandler extends IntentHandler {
	protected static final Logger LOG = LoggerFactory.getLogger(SwitchIntentHandler.class);

	private static final String SWITCH = "SwitchLights";
	private static final String SWITCH_SWITCH = "Switch";
	private static final String SWITCH_ACTION = "Action";
	private static final String SWITCH_LOCATION = "Location";

	public SwitchIntentHandler(OpenHabClient openHabClient, ItemDao itemDao) {
		super(openHabClient, itemDao);
	}

	@Override
	public SpeechletResponse handleIntent(Intent intent) {
		final Slot action = intent.getSlot(SWITCH_ACTION);
		final Slot myswitch = intent.getSlot(SWITCH_SWITCH);
		final Slot location = intent.getSlot(SWITCH_LOCATION);

		return buildSpeechletResponse("Switch",
				"Ok, switching " + action.getValue() + " the " + myswitch.getValue() + " in the " + location.getValue(),
				true);
	}

	@Override
	public String getIntentName() {
		return SWITCH;
	}

	@Override
	protected Logger getLogger() {
		return LOG;
	}

}

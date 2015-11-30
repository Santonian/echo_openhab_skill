package de.openhabskill.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.google.common.collect.Lists;

import de.openhabskill.client.OpenHabClient;
import de.openhabskill.entity.Item;
import de.openhabskill.entity.ItemDao;
import de.openhabskill.entity.ItemType;

/**
 * handles TV intents
 * 
 * @author Reinhard
 *
 */
public class TvIntentHandler extends IntentHandler {
	protected static final Logger LOG = LoggerFactory.getLogger(TvIntentHandler.class);

	private static final String TV = "OperateTv";

	// this could be extended for multiple harmony controlled tvs
	private static final String LOCATION = "living room";

	private static final String DEFAULT_COMMAND = "ON";
	private static final String CHANNEL_ITEM_NAME = "CHANNEL";

	public TvIntentHandler(OpenHabClient openHabClient, ItemDao itemDao) {
		super(openHabClient, itemDao);
	}

	@Override
	public SpeechletResponse handleIntentInternal(Intent intent) {
		final Slot action = intent.getSlot(SLOT_ACTION);
		final Slot channel = intent.getSlot(SLOT_CHANNEL);

		// Only one Slot can be filled
		if (action != null && action.getValue() != null) {
			final Item item = itemDao.findItem(LOCATION, action.getValue(), ItemType.TV);
			if (item != null) {
				openHabClient.sendCommand(item.getOpenHabItem(), DEFAULT_COMMAND);
			}
		} else if (channel != null && channel.getValue() != null) {
			final Item item = itemDao.findItem(LOCATION, CHANNEL_ITEM_NAME, ItemType.TV);
			if (item != null) {
				openHabClient.sendCommand(item.getOpenHabItem(), channel.getValue());
			}
		}

		return randomOkResponse();
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

	@Override
	protected List<String> getOptionalSlotNames() {
		return Lists.newArrayList(SLOT_ACTION, SLOT_CHANNEL);
	}
}

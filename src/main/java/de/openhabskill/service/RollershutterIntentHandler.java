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
 * Handles intent for an openHab RollershutterItem
 * 
 * @author Reinhard
 *
 */
public class RollershutterIntentHandler extends IntentHandler {
	protected static final Logger LOG = LoggerFactory.getLogger(RollershutterIntentHandler.class);

	private static final String ROLLERSHUTTER = "Rollershutter";

	private static final String DEFAULT_ITEM_NAME = "all";

	public RollershutterIntentHandler(OpenHabClient openHabClient, ItemDao itemDao) {
		super(openHabClient, itemDao);
		commandAlternatives.add(new CommandAlternative("UP", new String[] { "up", "raise" }));
		commandAlternatives.add(new CommandAlternative("DOWN", new String[] { "down", "lower" }));
		commandAlternatives.add(new CommandAlternative("STOP", new String[] { "stop", "halt" }));
	}

	@Override
	public SpeechletResponse handleIntentInternal(Intent intent) {
		final Slot location = intent.getSlot(SLOT_LOCATION);
		final Slot action = intent.getSlot(SLOT_ACTION);
		final Slot itemName = intent.getSlot(SLOT_ITEMNAME);

		final String itemNameStr = itemName.getValue() != null ? itemName.getValue() : DEFAULT_ITEM_NAME;

		final Item item = itemDao.findItem(location.getValue(), itemNameStr, ItemType.ROLLERSHUTTER);

		if (item != null) {

			final String actionStr = findCommand(action.getValue());

			if (!openHabClient.sendCommand(item.getOpenHabItem(), actionStr)) {
				return errorResponse(String.format(
						"I could not operate the shutter at location %s. There was a openHab communication error.",
						location.getValue()));
			}
		} else {
			return errorResponse(String.format("There is no item configured for the location %s and the itemname %s.",
					location.getValue(), itemNameStr));
		}

		return randomOkResponse();
	}

	@Override
	public String getIntentName() {
		return ROLLERSHUTTER;
	}

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	@Override
	protected List<String> getSlotNames() {
		return Lists.newArrayList(SLOT_LOCATION, SLOT_ACTION, SLOT_ITEMNAME);
	}

	@Override
	protected List<String> getOptionalSlotNames() {
		return Lists.newArrayList(SLOT_ITEMNAME);
	}
}

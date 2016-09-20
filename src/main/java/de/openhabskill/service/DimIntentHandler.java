package de.openhabskill.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.google.common.collect.Lists;

import de.openhabskill.entity.Item;
import de.openhabskill.entity.ItemType;

/**
 * Handles intent for an openHab Dimmer Item
 * 
 * @author Reinhard
 * 
 */
@Component
public class DimIntentHandler extends IntentHandler {
	protected static final Logger LOG = LoggerFactory.getLogger(DimIntentHandler.class);

	private static final String DIMM = "Dimm";

	@Override
	public SpeechletResponse handleIntentInternal(Intent intent) {
		final Slot percent = intent.getSlot(SLOT_PERCENT);
		final Slot itemName = intent.getSlot(SLOT_ITEMNAME);
		final Slot location = intent.getSlot(SLOT_LOCATION);

		Item item = itemRepository.findByLocationAndItemNameAndItemType(location.getValue(), itemName.getValue(),
				ItemType.DIMMER);
		// Try again with a stemmed version of item name
		if (item == null) {
			final String stemmedItemValue = stemmName(itemName.getValue());
			item = itemRepository.findByLocationAndItemNameAndItemType(location.getValue(), stemmedItemValue,
					ItemType.DIMMER);
		}

		if (item != null) {
			if (!openHabClient.sendCommand(item.getOpenHabItem(), percent.getValue())) {
				return errorResponse(String.format(
						"I could not operate the location %s and the itemname %s. There was a openHab communication error.",
						location.getValue(), itemName.getValue()));
			}
		} else {
			return errorResponse(String.format("There is no item configured for the location %s and the itemname %s.",
					location.getValue(), itemName.getValue()));
		}

		return randomOkResponse();
	}

	@Override
	public String getIntentName() {
		return DIMM;
	}

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	@Override
	protected List<String> getSlotNames() {
		return Lists.newArrayList(SLOT_PERCENT, SLOT_ITEMNAME, SLOT_LOCATION);
	}

}

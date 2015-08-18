package de.openhabskill.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.google.common.collect.Lists;

import de.openhabskill.client.OpenHabClient;
import de.openhabskill.entity.Item;
import de.openhabskill.entity.ItemDao;
import de.openhabskill.entity.ItemType;

/**
 * Handles intent for an openHab SwitchItem
 * 
 * @author Reinhard
 *
 */
public class SwitchIntentHandler extends IntentHandler {
	protected static final Logger LOG = LoggerFactory.getLogger(SwitchIntentHandler.class);

	private static final String SWITCH = "Switch";

	private SnowballStemmer stemmer;

	public SwitchIntentHandler(OpenHabClient openHabClient, ItemDao itemDao) {
		super(openHabClient, itemDao);
		stemmer = new englishStemmer();
	}

	@Override
	public SpeechletResponse handleIntentInternal(Intent intent) {
		final Slot action = intent.getSlot(SLOT_ACTION);
		final Slot itemName = intent.getSlot(SLOT_ITEMNAME);
		final Slot location = intent.getSlot(SLOT_LOCATION);

		Item item = itemDao.findItem(location.getValue(), itemName.getValue(), ItemType.SWITCH);
		// We assume, the stored itemName is singular, but the spoken word
		// sometime tends to be plural, so we try to stemm the word and try
		// again.
		if (item == null) {
			stemmer.setCurrent(itemName.getValue());
			stemmer.stem();
			final String stemmedItemValue = stemmer.getCurrent();
			LOG.debug("ItemName <{}> - Stemmed Value <{}>", itemName.getValue(), stemmedItemValue);
			item = itemDao.findItem(location.getValue(), stemmedItemValue, ItemType.SWITCH);
		}

		if (item != null) {
			if (!openHabClient.sendCommand(item.getOpenHabItem(), action.getValue())) {
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
		return SWITCH;
	}

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	@Override
	protected List<String> getSlotNames() {
		return Lists.newArrayList(SLOT_ACTION, SLOT_ITEMNAME, SLOT_LOCATION);
	}

}

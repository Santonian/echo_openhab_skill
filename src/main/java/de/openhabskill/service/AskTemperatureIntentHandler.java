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
 * Handles intent for a temperature request.
 * 
 * @author Reinhard
 *
 */
public class AskTemperatureIntentHandler extends IntentHandler {
	protected static final Logger LOG = LoggerFactory.getLogger(AskTemperatureIntentHandler.class);

	private static final String TEMPERATURE = "AskTemperature";

	private static final String ITEM_NAME = "temperature";

	public AskTemperatureIntentHandler(OpenHabClient openHabClient, ItemDao itemDao) {
		super(openHabClient, itemDao);
	}

	@Override
	public SpeechletResponse handleIntentInternal(Intent intent) {
		final Slot location = intent.getSlot(SLOT_LOCATION);

		final Item item = itemDao.findItem(location.getValue(), ITEM_NAME, ItemType.NUMBER);

		if (item != null) {
			final String tempStr = openHabClient.getState(item.getOpenHabItem());
			double temp = 0;
			try {
				temp = Double.parseDouble(tempStr);
			} catch (NumberFormatException nfe) {
				LOG.error("Value {} could not be pasrsed to double.", tempStr);
				return errorResponse(
						String.format("I could not get the temperature value for %s", location.getValue()));
			}

			return buildSpeechletResponse(
					String.format("The %s temperature is %.1f degrees.", location.getValue(), temp), true);
		} else {
			return errorResponse(
					String.format("There is no item configured for the location %s.", location.getValue()));
		}
	}

	@Override
	public String getIntentName() {
		return TEMPERATURE;
	}

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	@Override
	protected List<String> getSlotNames() {
		return Lists.newArrayList(SLOT_LOCATION);
	}

}

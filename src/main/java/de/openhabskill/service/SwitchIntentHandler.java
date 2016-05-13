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
import de.openhabskill.entity.ItemRepository;
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

    public SwitchIntentHandler(OpenHabClient openHabClient, ItemRepository itemDao) {
        super(openHabClient, itemDao);
    }

    @Override
    public SpeechletResponse handleIntentInternal(Intent intent) {
        final Slot action = intent.getSlot(SLOT_ACTION);
        final Slot itemName = intent.getSlot(SLOT_ITEMNAME);
        final Slot location = intent.getSlot(SLOT_LOCATION);

        Item item = itemRepository.findByLocationAndItemNameAndItemType(location.getValue(), itemName.getValue(),
                ItemType.SWITCH);
        // Try again with a stemmed version of item name
        if (item == null) {
            final String stemmedItemValue = stemmName(itemName.getValue());
            LOG.debug("ItemName <{}> - Stemmed Value <{}>", itemName.getValue(), stemmedItemValue);
            item = itemRepository.findByLocationAndItemNameAndItemType(location.getValue(), stemmedItemValue, ItemType.SWITCH);
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

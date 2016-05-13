package de.openhabskill.service;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.openhabskill.client.OpenHabClient;
import de.openhabskill.entity.Item;
import de.openhabskill.entity.ItemRepository;
import de.openhabskill.entity.ItemType;

/**
 * Handler for Color Intents. to Change RGB Lights.
 * 
 * @author Reinhard
 *
 */

public class ColorIntentHandler extends IntentHandler {
    protected static final Logger LOG = LoggerFactory.getLogger(ColorIntentHandler.class);

    private static final String COLOR_FILE = "./src/main/resources/colors/colors.csv";

    private static final String COLOR = "SetColor";
    private static final String COLOR_ITEM_NAME = "COLOR";

    private Map<String, RGB> colors = Maps.newHashMap();

    public ColorIntentHandler(OpenHabClient openHabClient, ItemRepository itemDao) {
        super(openHabClient, itemDao);
        importColors();
    }

    @Override
    public SpeechletResponse handleIntentInternal(Intent intent) {
        final Slot color = intent.getSlot(SLOT_COLOR);
        final Slot location = intent.getSlot(SLOT_LOCATION);

        final Item item = itemRepository.findByLocationAndItemNameAndItemType(location.getValue(), COLOR_ITEM_NAME,
                ItemType.COLOR);

        if (item != null) {
            if (colors.containsKey(color.getValue())) {
                final RGB rgb = colors.get(color.getValue());

                final float[] hsbVals = new float[3];
                Color.RGBtoHSB(rgb.getR(), rgb.getG(), rgb.getB(), hsbVals);
                // openhab works with a hsbType that needs a little correction
                final BigDecimal hue = BigDecimal.valueOf(hsbVals[0] * 360);
                final BigDecimal saturation = BigDecimal.valueOf(hsbVals[1] * 100);
                final BigDecimal value = BigDecimal.valueOf(hsbVals[2] * 100);

                if (!openHabClient.sendCommand(item.getOpenHabItem(), String.format("%.0f,%.0f,%.0f", hue, saturation, value))) {
                    return errorResponse(
                            String.format("I could not set the color %s at locatoin %s. There was a openHab communication error.",
                                    color.getValue(), location.getValue()));
                }
            } else {
                return errorResponse(String.format("I am sorry, I don't know the color %s.", color.getValue()));
            }
        } else {
            return errorResponse(String.format("There is no item configured for the location %s.", location.getValue()));
        }

        return randomOkResponse();
    }

    @Override
    public String getIntentName() {
        return COLOR;
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected List<String> getSlotNames() {
        return Lists.newArrayList(SLOT_COLOR, SLOT_LOCATION);
    }

    private void importColors() {
        String line;

        try {
            final BufferedReader br = new BufferedReader(new FileReader(COLOR_FILE));
            while ((line = br.readLine()) != null) {
                final String[] colorInfo = line.split(",");
                colors.put(colorInfo[0].toLowerCase(), new RGB(colorInfo[1], colorInfo[2], colorInfo[3]));
            }
            LOG.info("Initialized {} colors", colors.size());
            br.close();
        } catch (FileNotFoundException e) {
            LOG.error("Color File {} not found", COLOR_FILE, e);
        } catch (IOException e) {
            LOG.error("Read error while reading colors from file", e);
        }
    }

    private class RGB {

        private Integer r;
        private Integer g;
        private Integer b;

        public RGB(final String r, final String g, final String b) {
            this.r = Integer.valueOf(r);
            this.g = Integer.valueOf(g);
            this.b = Integer.valueOf(b);
        }

        public Integer getR() {
            return r;
        }

        public Integer getG() {
            return g;
        }

        public Integer getB() {
            return b;
        }

    }
}

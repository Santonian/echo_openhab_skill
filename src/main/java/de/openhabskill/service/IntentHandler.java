package de.openhabskill.service;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.google.common.collect.Lists;

import de.openhabskill.AlexaOpenHabSkillApplication;
import de.openhabskill.client.OpenHabClient;
import de.openhabskill.entity.ItemRepository;

/**
 * abstract base class for all intent handler
 * 
 * @author Reinhard
 *
 */
public abstract class IntentHandler {

    protected static final String SLOT_ITEMNAME = "ItemName";
    protected static final String SLOT_ACTION = "Action";
    protected static final String SLOT_LOCATION = "Location";
    protected static final String SLOT_CHANNEL = "Channel";
    protected static final String SLOT_PERCENT = "Percent";
    protected static final String SLOT_COLOR = "Color";

    final protected OpenHabClient openHabClient;

    final protected ItemRepository itemRepository;

    final private List<String> okValues = Lists.newArrayList("Ok", "Allright", "Done!", "Will do", "as you wish");

    final protected SnowballStemmer stemmer;

    final protected List<CommandAlternative> commandAlternatives = Lists.newArrayList();

    public IntentHandler(final OpenHabClient openHabClient, final ItemRepository itemRepository) {
        this.openHabClient = openHabClient;
        this.itemRepository = itemRepository;
        this.stemmer = new englishStemmer();

        getLogger().debug("IntentHandler created.");
    }

    public boolean isResponsible(final String intentName) {
        return getIntentName().equals(intentName);
    }

    public SpeechletResponse handleIntent(final Intent intent) {
        logIntent(intent);
        if (!validateSlots(intent)) {
            return errorResponse("Sorry, I could not process your request. Please try again.");
        }
        return handleIntentInternal(intent);
    }

    public abstract SpeechletResponse handleIntentInternal(final Intent intent);

    /**
     * @return Name of the Intent this {@link IntentHandler} handles
     */
    public abstract String getIntentName();

    protected abstract Logger getLogger();

    protected abstract List<String> getSlotNames();

    /**
     * Validates all relevant slots for notNull
     * 
     */
    protected boolean validateSlots(final Intent intent) {
        boolean failure = false;
        for (String sn : getSlotNames()) {
            // only check mandatory values
            if (!getOptionalSlotNames().contains(sn)) {
                final Slot slot = intent.getSlot(sn);
                failure |= slot.getValue() == null;
            }
        }
        return !failure;
    }

    protected String findCommand(final String action) {
        for (CommandAlternative ca : commandAlternatives) {
            if (ca.isResponsible(action)) {
                return ca.getCommand();
            }
        }
        return action;
    }

    private void logIntent(final Intent intent) {
        final StringBuffer sb = new StringBuffer("HandleIntent: ");
        sb.append(intent.getName());
        sb.append(" ");
        for (String sn : getSlotNames()) {
            sb.append(" - ");
            sb.append(sn);
            sb.append("<");
            sb.append(intent.getSlot(sn) != null ? intent.getSlot(sn).getValue() : "NULL SLOT");
            sb.append(">");
        }
        getLogger().info(sb.toString());
    }

    protected SpeechletResponse errorResponse(final String error) {
        return buildSpeechletResponse(error, true);
    }

    protected SpeechletResponse randomOkResponse() {
        final Random r = new Random(new Date().getTime());
        final String okValue = okValues.get(r.nextInt(okValues.size()));

        return buildSpeechletResponse(okValue, true);
    }

    /**
     * Can be overriden to allow optional slot values
     * 
     * @return
     */
    protected List<String> getOptionalSlotNames() {
        return Lists.newArrayList();
    }

    static public SpeechletResponse buildSpeechletResponse(final String output, final boolean shouldEndSession) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle(AlexaOpenHabSkillApplication.SPEECHLET_APP_NAME);
        card.setContent(String.format("%s", output));

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(output);

        // Create the speechlet response.
        SpeechletResponse response = new SpeechletResponse();
        response.setShouldEndSession(shouldEndSession);
        response.setOutputSpeech(speech);
        response.setCard(card);
        return response;
    }

    /**
     * Try to transform a plural to a singular name
     * 
     * @param name
     * @return
     */
    protected String stemmName(final String name) {
        stemmer.setCurrent(name);
        stemmer.stem();
        final String stemmedName = stemmer.getCurrent();
        getLogger().debug("ItemName <{}> - Stemmed Value <{}>", name, stemmedName);
        return stemmedName;
    }

}

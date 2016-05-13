package de.openhabskill.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import de.openhabskill.OpenHabConfiguration;
import lombok.extern.slf4j.Slf4j;

/**
 * calls a openHab server per REST call to send a command to an item
 * 
 * @author Reinhard
 *
 */
@Slf4j
@Component
public class OpenHabClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    OpenHabConfiguration openHabConfiguration;

    public boolean sendCommand(final String openHabItemName, final String command) {

        log.debug("Send to OpenHab item {} command {}", openHabItemName, command.toUpperCase());
        final ResponseEntity<Object> response = restTemplate.postForEntity(getBaseURI().path(openHabItemName).build().toUri(),
                command.toUpperCase(), Object.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            log.debug("OpenHab communication ok");
            return true;
        } else {
            log.error("OpenHab Status Error {} - {}", response.getStatusCode().value(),
                    response.getStatusCode().getReasonPhrase());
            return false;
        }
    }

    /**
     * Returns the state of an openHab Item
     * 
     */
    public String getState(final String openHabItemName) {

        String state = "";
        try {
            restTemplate.getForObject(getBaseURI().path(openHabItemName).path("state").build().toUri(), String.class);
        } catch (RestClientException e) {
            log.error("Error getting State for Item {} ", openHabItemName, e);
        }

        return state;

    }

    private UriComponentsBuilder getBaseURI() {
        return UriComponentsBuilder.fromPath(
                String.format("http://%s:%d/rest/items", openHabConfiguration.getHost(), openHabConfiguration.getPort()));
    }
}

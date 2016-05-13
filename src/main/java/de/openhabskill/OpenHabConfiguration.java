package de.openhabskill;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * configuration class for the openHab Server
 * 
 * @author Reinhard
 *
 */
@Data
@Configuration
@ConfigurationProperties(value = "openHab")
public class OpenHabConfiguration {

    private String host;

    private Integer port;

}

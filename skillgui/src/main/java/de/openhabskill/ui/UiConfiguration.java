package de.openhabskill.ui;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * configuration class for the ui. currently only for the http port
 * 
 * @author Reinhard
 *
 */
@Data
@Configuration
@ConfigurationProperties(value = "ui")
public class UiConfiguration {
	private Integer port;
}

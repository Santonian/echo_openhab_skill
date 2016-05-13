package de.openhabskill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AlexaOpenHabSkillApplication {
    public static final String SPEECHLET_APP_NAME = "Alexa OpenHab Skill";

    public static void main(String[] args) {
        SpringApplication.run(AlexaOpenHabSkillApplication.class, args);
    }
}

package de.openhabskill;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import de.thomaskrille.dropwizard_template_config.TemplateConfigBundle;

public class EchoOpenhabSkillApplication extends Application<EchoOpenhabSkillConfiguration> {

    public static void main(String[] args) throws Exception {
        new EchoOpenhabSkillApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<EchoOpenhabSkillConfiguration> bootstrap) {
        final AssetsBundle bundle = new AssetsBundle("/html", "/");
        bootstrap.addBundle(bundle);
        bootstrap.addBundle(new TemplateConfigBundle());
    }

    @Override
    public void run(EchoOpenhabSkillConfiguration configuration, Environment environment) throws Exception {

        environment.jersey().register(new LivingRoomResource());

    }

}

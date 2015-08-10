package de.openhabskill;

import javax.ws.rs.client.Client;

import com.mongodb.ServerAddress;

import de.thomaskrille.dropwizard_template_config.TemplateConfigBundle;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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

		final DatabaseConfiguration databaseConfiguration = configuration.getDatabase();

		final ServerAddress address;
		if (databaseConfiguration.getPort() == null) {
			address = new ServerAddress(databaseConfiguration.getHost());
		} else {
			address = new ServerAddress(databaseConfiguration.getHost(), databaseConfiguration.getPort());
		}

		// if (databaseConfiguration.getUser().isEmpty() &&
		// databaseConfiguration.getPassword().isEmpty()) {
		// environment.jersey().register(new CookItResource(new
		// MongoClient(address)));
		// } else {
		// final MongoCredential credentials =
		// MongoCredential.createCredential(databaseConfiguration.getUser(),
		// databaseConfiguration.getDatabase(),
		// databaseConfiguration.getPassword().toCharArray());
		// environment.jersey()
		// .register(new CookItResource(new MongoClient(address,
		// Lists.newArrayList(credentials))));
		// }

		final Client httpClient = new JerseyClientBuilder(environment)
				.using(configuration.getJerseyClientConfiguration()).build(getName());

		environment.jersey().register(new OpenHabResource(new OpenHabClient(httpClient)));

	}

}

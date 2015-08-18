package de.openhabskill;

import javax.ws.rs.client.Client;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import de.openhabskill.client.OpenHabClient;
import de.openhabskill.entity.ItemDao;
import de.openhabskill.resource.ItemResource;
import de.openhabskill.resource.OpenHabResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class EchoOpenhabSkillApplication extends Application<EchoOpenhabSkillConfiguration> {

	public static String SPEECHLET_APP_NAME = "OpenHab Skill";

	public static void main(String[] args) throws Exception {
		new EchoOpenhabSkillApplication().run(args);
	}

	@Override
	public void initialize(Bootstrap<EchoOpenhabSkillConfiguration> bootstrap) {
		final AssetsBundle bundle = new AssetsBundle("/html", "/");
		bootstrap.addBundle(bundle);
	}

	@Override
	public void run(EchoOpenhabSkillConfiguration configuration, Environment environment) throws Exception {

		final OpenHabClient openHabClient = createOpenHabClient(configuration, environment);

		final ItemDao itemDao = new ItemDao(createMongoClient(configuration));

		environment.jersey().register(new OpenHabResource(openHabClient, itemDao));
		environment.jersey().register(new ItemResource(itemDao));
	}

	private OpenHabClient createOpenHabClient(EchoOpenhabSkillConfiguration configuration, Environment environment) {
		final Client httpClient = new JerseyClientBuilder(environment)
				.using(configuration.getJerseyClientConfiguration()).build(getName());

		final OpenHabConfiguration openHab = configuration.getOpenHab();

		return new OpenHabClient(openHab.getHost(), openHab.getPort(), httpClient);
	}

	private MongoClient createMongoClient(EchoOpenhabSkillConfiguration configuration) throws Exception {
		final DatabaseConfiguration databaseConfiguration = configuration.getDatabase();

		final ServerAddress address;
		if (databaseConfiguration.getPort() == null) {
			address = new ServerAddress(databaseConfiguration.getHost());
		} else {
			address = new ServerAddress(databaseConfiguration.getHost(), databaseConfiguration.getPort());
		}

		if (databaseConfiguration.getUser().isEmpty() && databaseConfiguration.getPassword().isEmpty()) {
			return new MongoClient(address);
		} else {
			final MongoCredential credentials = MongoCredential.createCredential(databaseConfiguration.getUser(),
					databaseConfiguration.getDatabase(), databaseConfiguration.getPassword().toCharArray());
			return new MongoClient(address, Lists.newArrayList(credentials));
		}
	}
}

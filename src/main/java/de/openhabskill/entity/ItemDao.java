package de.openhabskill.entity;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

public class ItemDao {

	private Datastore datastore;

	public ItemDao(MongoClient mongoClient) {
		datastore = new Morphia().createDatastore(mongoClient, "openhabskill");
	}

	public Item save(final Item item) {
		datastore.save(item);

		return item;
	}

	public void delete(final Item item) {
		datastore.delete(item);
	}

	public Item findItem(final String location, final String itemName, final ItemType itemType) {

		final Item item = datastore.find(Item.class).filter("location =", location).filter("itemName =", itemName)
				.filter("itemType =", itemType).get();

		return item;
	}

	public Item findItem(final String location, final ItemType itemType) {

		final Item item = datastore.find(Item.class).filter("location=", location).filter("itemType =", itemType).get();

		return item;
	}
}

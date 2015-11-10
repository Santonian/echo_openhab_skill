package de.openhabskill.entity;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

/**
 * DAO Class for accessing {@link Item} Object in the database
 * 
 * @author Reinhard
 *
 */
public class ItemDao {

	private Datastore datastore;

	public ItemDao(MongoClient mongoClient) {
		datastore = new Morphia().createDatastore(mongoClient, "openhabskill");
	}

	/**
	 * saves the {@link Item}
	 * 
	 * @param item
	 * @return - the saved item (id field ist filled)
	 */
	public Item save(final Item item) {
		datastore.save(item);

		return item;
	}

	/**
	 * deletes item from the database by id
	 * 
	 * @param id
	 * @return <code>true</code> if an item is deleted, <code>false</code> if no
	 *         item got deleted
	 */
	public boolean delete(final String id) {
		final WriteResult result = datastore.delete(Item.class, new ObjectId(id));

		return result.getN() > 0;
	}

	/**
	 * finds {@link Item} by location, itemName and itemType
	 * 
	 * @return <code>null</code> if not item found, otherwise the item
	 */
	public Item findItem(final String location, final String itemName, final ItemType itemType) {

		final Item item = datastore.find(Item.class).filter("location =", location).filter("itemName =", itemName)
				.filter("itemType =", itemType).get();

		return item;
	}

	/**
	 * finds {@link Item} by location and itemType
	 * 
	 * @return <code>null</code> if not item found, otherwise the item
	 */
	public Item findItem(final String location, final ItemType itemType) {

		final Item item = datastore.find(Item.class).filter("location=", location).filter("itemType =", itemType).get();

		return item;
	}

	/**
	 * loads all items
	 * 
	 * @return {@link List} of {@link Item}s
	 */
	public List<Item> getItems() {
		return datastore.find(Item.class).asList();
	}

}

package de.openhabskill.entity;

import org.mongodb.morphia.annotations.Id;

/**
 * Represents an Open Hab Item
 * 
 * @author Reinhard
 *
 */
public class Item {
	@Id
	public String id;

	/**
	 * The name of the item in openhab
	 */
	public String openHabItem;

	/**
	 * the Itemtype (switch, temperature, rollershutter, ...)
	 */
	private ItemType itemType;

	/**
	 * itemname to identifiy the item inside a location. This is the spoken name
	 * to alexa
	 * 
	 */
	private String itemName;

	/**
	 * item location. This is the location which is spoken to alexa
	 * 
	 */
	private String location;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpenHabItem() {
		return openHabItem;
	}

	public void setOpenHabItem(String openHabItem) {
		this.openHabItem = openHabItem;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}

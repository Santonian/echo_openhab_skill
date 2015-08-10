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

	private String itemName;

	private ItemType itemType;

	private String location;

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

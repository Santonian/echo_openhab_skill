package de.openhabskill.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * Represents an Open Hab Item
 * 
 * @author Reinhard
 *
 */
@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    /**
     * The name of the item in openhab
     */
    public String openHabItem;

    /**
     * the Itemtype (switch, temperature, rollershutter, ...)
     */
    private ItemType itemType;

    /**
     * itemname to identifiy the item inside a location. This is the spoken name to alexa
     * 
     */
    private String itemName;

    /**
     * item location. This is the location which is spoken to alexa
     * 
     */
    private String location;

}

package de.openhabskill.entity;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data Repository for CRUD {@link Item} operations
 * 
 * @author Reinhard
 *
 */
public interface ItemRepository extends CrudRepository<Item, Integer> {
    Item findByLocationAndItemNameAndItemType(String location, String itemName, ItemType itemType);
}

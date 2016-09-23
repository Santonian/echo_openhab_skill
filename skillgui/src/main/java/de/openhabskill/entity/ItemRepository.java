package de.openhabskill.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data Repository for CRUD {@link Item} operations
 * 
 * @author Reinhard
 *
 */
public interface ItemRepository extends JpaRepository<Item, Integer> {
	Item findByLocationAndItemNameAndItemType(String location, String itemName, ItemType itemType);
}

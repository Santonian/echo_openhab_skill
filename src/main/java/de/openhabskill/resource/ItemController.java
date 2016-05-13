package de.openhabskill.resource;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.openhabskill.entity.Item;
import de.openhabskill.entity.ItemRepository;

/**
 * REST interface for the UI to store openHab items in the mongoDB
 * 
 * @author Reinhard
 *
 */
@RestController
@RequestMapping(value = "/item")
public class ItemController {
    @Autowired
    private ItemRepository itemRepository;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Item>> getItem() {
        Iterable<Item> items = itemRepository.findAll();
        return ResponseEntity.ok(items);
    }

    /**
     * stores an item in the database. Checks wether or not the item is already saved.<br>
     * 
     * @param item
     *            the {@link Item}
     * @return HTTP Status 201 if item created successfull HTTP Status 409 if item is already saved
     * 
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Item> storeItem(@RequestBody Item item) {

        final Item oldItem = itemRepository.findByLocationAndItemNameAndItemType(item.getLocation(), item.getItemName(),
                item.getItemType());

        if (oldItem == null) {
            itemRepository.save(item);
            return ResponseEntity.created(URI.create(item.getId().toString())).body(item);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(oldItem);
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void deleteItem(@PathVariable("id") final Integer id) {
        itemRepository.delete(id);
    }

}

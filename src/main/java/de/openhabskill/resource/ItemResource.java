package de.openhabskill.resource;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.openhabskill.entity.Item;
import de.openhabskill.entity.ItemDao;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
public class ItemResource {
	private ItemDao itemDao;

	public ItemResource(final ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getItem() {

		final List<Item> items = itemDao.getItems();

		return Response.ok(items).build();
	}

	/**
	 * stores an item in the database. Checks wether or not the item is already
	 * saved.<br>
	 * 
	 * @param item
	 *            the {@link Item}
	 * @return HTTP Status 201 if item created successfull HTTP Status 409 if
	 *         item is already saved
	 * 
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response storeItem(Item item) {

		final Item oldItem = itemDao.findItem(item.getLocation(), item.getItemName(), item.getItemType());

		if (oldItem == null) {
			itemDao.save(item);
			return Response.created(URI.create(item.getId())).entity(item).build();
		} else {
			return Response.status(Status.CONFLICT).build();
		}
	}

	@DELETE
	@Path("{id}")
	public Response deleteItem(@PathParam("id") final String id) {
		if (itemDao.delete(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}

	}

}

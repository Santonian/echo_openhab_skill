package de.openhabskill.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	@Path("ping")
	public String ping() {
		return "ok";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response storeItem(Item item) {
		itemDao.save(item);

		return Response.created(URI.create(item.getId())).entity(item).build();
	}

}

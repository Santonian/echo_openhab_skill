package de.openhabskill;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/livingroom")
@Produces(MediaType.APPLICATION_JSON)
public class LivingRoomResource {

    public LivingRoomResource() {

    }

    @GET
    @Path("ping")
    public String ping() {
        return "ok";
    }

}

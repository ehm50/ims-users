package org.digam.users.boundary;

import java.net.URI;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.digam.users.entity.User;

@Path("users")
public class UsersResource {

	@Inject
	private UsersService service;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getAll() {
		return service.getAll();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(User newUser, @Context UriInfo uriInfo) {
		service.add(newUser);
		return Response.created(getLocation(uriInfo, newUser.getId())).build();
	}
	
	 URI getLocation(UriInfo uriInfo, int id) {
	        return uriInfo.getAbsolutePathBuilder().path("" + id).build();
	    }

}

package org.digam.users.boundary;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.digam.users.entity.User;

@Path("users")
public class UsersResource {

	@Inject
	private UsersService service;

	@GET
	public Response getAll() {
		List<User> users = service.getAll(); // queries database for all users
		GenericEntity<List<User>> list = new GenericEntity<List<User>>(users) {
		};
		return Response.ok(list).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(User newUser, @Context UriInfo uriInfo) {
		service.add(newUser);
		return Response.created(getLocation(uriInfo, newUser.getId())).build();
	}

	@GET
	@Path("{id}")
	public Response get(@PathParam("id") Long id) {
		System.out.println("request for " + id);
		final Optional<User> userFound = service.get(id);
		if (userFound.isPresent()) {
			return Response.ok(userFound.get()).build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, User updated) {
		updated.setId(id);
		boolean done = service.update(updated);
		return done ? Response.ok(updated).build() : Response.status(Response.Status.NOT_FOUND).build();
	}

	@DELETE
	@Path("{id}")
	public Response remove(@PathParam("id") Long id) {
		service.remove(id);
		return Response.ok().build();
	}

	URI getLocation(UriInfo uriInfo, Long id) {
		return uriInfo.getAbsolutePathBuilder().path("" + id).build();
	}

}

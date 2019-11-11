package org.digam.users.boundary;

import java.net.URI;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.digam.security.boundary.TokenIssuer;
import org.digam.users.entity.Credential;
import org.digam.users.entity.User;

@Path("users")
public class UsersResource {

	@Inject
	private UsersService service;

	@Inject
	private TokenIssuer issuer;

	@GET
	public Response getAll() {
		return Response.ok(service.getAll()).build();
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

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(User newUser, @Context UriInfo uriInfo) {
		service.add(newUser);
		return Response.created(getLocation(uriInfo, newUser.getId())).build();
	}

	@Path("/authenticate")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticate(Credential creds) {
		User validUser = service.isValid(creds.getUsername(), creds.getPassword());

		if (validUser != null) {
			String token = issuer.issueToken(creds.getUsername());
			// Set token and user id as part of response
			JsonObject json = Json.createObjectBuilder().add("id", validUser.getId()).add("token", token).build();

			return Response.ok(json).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
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

package fr.middlewaresolutions.demo.services;

import fr.middlewaresolutions.demo.converters.ClientConverter;
import fr.middlewaresolutions.demo.dtos.ClientDto;
import fr.middlewaresolutions.demo.models.Client;
import fr.middlewaresolutions.demo.repositories.ClientsRepository;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * API service for {@link Client Clients}.
 */
@Path("/clients")
public class ClientsService {

  @Inject ClientsRepository clientsRepository;
  @Inject ClientConverter clientConverter;

  /**
   * List all clients.
   * @param page optional page.
   * @param limit optional page limit.
   * @return a list of clients.
   */
  @GET
  @Produces("application/json")
  public Response listClients(
      @QueryParam("page") Integer page,
      @QueryParam("limit") Integer limit
  ) {
    List<Client> clients = clientsRepository.findAll(page, limit);
    return Response.ok(clientConverter.toDtos(clients)).build();
  }

  /**
   * Get a client
   * @param email email to fetch.
   * @return 404 if not found.
   */
  @GET
  @Path("/{email}")
  @Produces("application/json")
  public Response getClient(@PathParam("email") String email) {
    Client client = clientsRepository.findByEmail(email);
    if (client == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    Log.info("Client " + client + " was read");
    return Response.ok(clientConverter.toDto(client)).build();
  }

  /**
   * Create a client.
   * @param dto DTO in the body.
   * @return 409 if the DTO's email already exists.
   */
  @POST
  @Consumes("application/json")
  @Transactional
  public Response createClient(ClientDto dto) {
    // Must not exist
    if(clientsRepository.findByEmail(dto.getEmail()) != null) {
      return Response.status(Response.Status.CONFLICT).build();
    }

    Client client = clientConverter.toModel(dto);
    clientsRepository.persist(client);
    Log.info("Client with email " + client + " was created");

    return Response.status(Response.Status.CREATED).build();
  }

  /**
   * Update a client.
   * @param email client identifier.
   * @param dto DTO in the body.
   * @return 400 if email changed. 404 if not found. 202 if ok.
   */
  @POST
  @Path("/{email}")
  @Consumes("application/json")
  @Transactional
  public Response updateClient(@PathParam("email") String email, ClientDto dto) {
    // Emails must match
    if(!dto.getEmail().equals(email)) {
      Log.warn("Email do not match.");
      return Response.status(Response.Status.BAD_REQUEST).build();
    }

    // Must exist
    if(clientsRepository.findByEmail(email) == null) {
      Log.warn("Cannot update : not found.");
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    // Update & persists
    Client client = clientConverter.toModel(dto);
    clientsRepository.persist(client);

    return Response.status(Response.Status.ACCEPTED).build();
  }

  /**
   * Delete a client.
   * @param email email of the client to delete.
   * @return 202. Will never return 404.
   */
  @DELETE
  @Path("/{email}")
  @Transactional
  public Response deleteClient(@PathParam("email") String email) {
    Client client = clientsRepository.findByEmail(email);
    if (client != null) {
      clientsRepository.delete(client);
      Log.info("Client with email " + email + " was deleted");
    }
    return Response.status(Response.Status.ACCEPTED).build();
  }

}

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

  @GET
  @Produces("application/json")
  public Response listClients(
      @QueryParam("page") Integer page,
      @QueryParam("limit") Integer limit
  ) {
    List<Client> clients = clientsRepository.findAll(page, limit);
    return Response.ok(clientConverter.toDtos(clients)).build();
  }

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

  @POST
  @Consumes("application/json")
  @Transactional
  public Response createClient(ClientDto clientDto) {
    // Must not exist
    if(clientsRepository.findByEmail(clientDto.getEmail()) != null) {
      return Response.status(Response.Status.CONFLICT).build();
    }

    Client client = clientConverter.toModel(clientDto);
    clientsRepository.persist(client);
    Log.info("Client with email " + client + " was created");

    return Response.status(Response.Status.CREATED).build();
  }

  @POST
  @Path("/{email}")
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

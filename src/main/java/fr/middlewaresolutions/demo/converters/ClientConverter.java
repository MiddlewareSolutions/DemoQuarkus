package fr.middlewaresolutions.demo.converters;

import fr.middlewaresolutions.demo.dtos.ClientDto;
import fr.middlewaresolutions.demo.models.Client;
import fr.middlewaresolutions.demo.repositories.ClientsRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Used to convert clients into useful DTO.
 */
@ApplicationScoped
public class ClientConverter {

  @Inject ClientsRepository repository;

  public @NotNull ClientDto toDto(@NotNull Client client) {
    ClientDto dto = new ClientDto();
    dto.setEmail(client.getEmail());
    dto.setFirstName(client.getFirstName());
    dto.setLastName(client.getLastName());
    return dto;
  }

  public @NotNull List<ClientDto> toDtos(@NotNull List<Client> clients) {
    return clients.stream().map(this::toDto).toList();
  }

  public @NotNull Client toModel(@NotNull ClientDto dto) {
    // Try to find the client
    Client client = repository.findByEmail(dto.getEmail());
    if(client == null) {
      client = new Client();
      client.setEmail(dto.getEmail());
    }

    // Update the rest of the values
    client.setFirstName(dto.getFirstName());
    client.setLastName(dto.getLastName());

    return client;
  }

  public @NotNull List<Client> toModels(@NotNull List<ClientDto> dtos) {
    return dtos.stream().map(this::toModel).toList();
  }

}

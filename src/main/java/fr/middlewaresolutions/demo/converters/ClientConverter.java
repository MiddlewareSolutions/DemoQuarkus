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

  /**
   * Convert a model to a DTO.
   * @param client model to convert.
   * @return a new DTO.
   */
  public @NotNull ClientDto toDto(@NotNull Client client) {
    ClientDto dto = new ClientDto();
    dto.setEmail(client.getEmail());
    dto.setFirstName(client.getFirstName());
    dto.setLastName(client.getLastName());
    return dto;
  }

  /**
   * Convert a list of models to a list of dtos.
   * @param clients a list of models.
   * @return a new list.
   * @see #toDto(Client)
   */
  public @NotNull List<ClientDto> toDtos(@NotNull List<Client> clients) {
    return clients.stream().map(this::toDto).toList();
  }

  /**
   * Convert a DTO to a model. Will update an existing one if needed.
   * @param dto DTO to convert.
   * @return existing model if found. A new instance (transient) if not.
   */
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

  /**
   * Convert a list of DTOs to a list of models.
   * @param dtos a list of DTOs.
   * @return a new list.
   * @see #toModel(ClientDto)
   */
  public @NotNull List<Client> toModels(@NotNull List<ClientDto> dtos) {
    return dtos.stream().map(this::toModel).toList();
  }

}

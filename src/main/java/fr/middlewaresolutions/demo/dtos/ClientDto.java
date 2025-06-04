package fr.middlewaresolutions.demo.dtos;

import lombok.Data;

/**
 * Client representation, on the network.
 */
@Data
public class ClientDto {
  private String firstName;
  private String lastName;
  private String email;
}
